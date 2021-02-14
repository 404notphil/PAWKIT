//
// Created by Philip Carlson on 2/13/21.
//

#include "NativePlayer.h"
#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#include <android/log.h>


#include <cstring>
#include <jni.h>
#include <string>
#include "../../../../iolib/src/main/cpp/player/SimpleMultiPlayer.h"
#include "../../../../parselib/src/main/cpp/wav/WavStreamReader.h"
#include "../../../../parselib/src/main/cpp/stream/MemInputStream.h"


//#ifdef __cplusplus
extern "C" {
//#endif

using namespace iolib;
using namespace parselib;

static const char* TAG = "DrumPlayerJNI";

static SimpleMultiPlayer sDTPlayer;

JNIEXPORT jboolean JNICALL
Java_com_tunepruner_bomboleguerodemo_instrument_JNIPlayerAdapter_loadWavAssetNative(
        JNIEnv* env, jobject, jbyteArray bytearray, jint index, jfloat pan, jint channels) {
    int len = env->GetArrayLength (bytearray);

    unsigned char* buf = new unsigned char[len];
    env->GetByteArrayRegion (bytearray, 0, len, reinterpret_cast<jbyte*>(buf));

    MemInputStream stream(buf, len);

    WavStreamReader reader(&stream);
    reader.parse();

    int actualChannels = reader.getNumChannels();

    jboolean isFormatValid = actualChannels == channels;

    SampleBuffer* sampleBuffer = new SampleBuffer();
    sampleBuffer->loadSampleData(&reader);

    OneShotSampleSource* source = new OneShotSampleSource(sampleBuffer, pan);
    sDTPlayer.addSampleSource(source, sampleBuffer);

    delete[] buf;

    return isFormatValid;
}

JNIEXPORT void JNICALL
Java_com_tunepruner_bomboleguerodemo_instrument_JNIPlayerAdapter_trigger(JNIEnv* env, jobject, jint index) {
    sDTPlayer.triggerDown(index);
}


JNIEXPORT void JNICALL
Java_com_tunepruner_bomboleguerodemo_instrument_JNIPlayerAdapter_unloadWavAssetsNative(JNIEnv* env, jobject){

}

JNIEXPORT void JNICALL
Java_com_tunepruner_bomboleguerodemo_instrument_JNIPlayerAdapter_setupAudioStreamNative(JNIEnv* env, jobject, jint numChannels){
    sDTPlayer.setupAudioStream(numChannels);
    sDTPlayer.startStream();
}

JNIEXPORT void JNICALL
Java_com_tunepruner_bomboleguerodemo_instrument_JNIPlayerAdapter_startAudioStreamNative(JNIEnv* env, jobject){

}

JNIEXPORT void JNICALL
Java_com_tunepruner_bomboleguerodemo_instrument_JNIPlayerAdapter_teardownAudioStreamNative(JNIEnv* env, jobject){

}

//#ifdef __cplusplus
}
//#endif
