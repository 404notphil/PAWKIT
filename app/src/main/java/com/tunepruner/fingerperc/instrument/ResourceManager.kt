package com.tunepruner.fingerperc.instrument

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager

class ResourceManager(val context: Context, val libraryName: String) {
    private var counter: Int = 0
    val fileSnapshots = ArrayList<FileSnapshot>()

    init {
        analyzeFiles(context)
    }

    private fun analyzeFiles(context: Context) {
        val assetManager: AssetManager = context.assets
        val filePaths = assetManager.list("audio/")
            ?: error("AssetManager couldn't get filePaths")
        for (element in filePaths) {
            val filename = "audio/${element}"
            val afd: AssetFileDescriptor =
                assetManager.openFd(filename)
            val fileSnapshot = filenameToSnapshot(filename, afd, assetManager)
            fileSnapshots.add(fileSnapshot)
        }
    }


    private fun filenameToSnapshot(
        fileName: String,
        assetFileDescriptor: AssetFileDescriptor,
        assetManager: AssetManager
    ): FileSnapshot {

        val trimDirectories = fileName.split("/")
        val trimExtension = trimDirectories[trimDirectories.size-1].split(".")
        val fileNameMembers = trimExtension[0].split("_")

        return FileSnapshot(
            counter++,
            fileName,
            fileNameMembers[0].toInt(),
            fileNameMembers[1].toInt(),
            fileNameMembers[2].toInt(),
            fileNameMembers[3],
            assetFileDescriptor,
            assetManager
        )

    }

    fun getGroupCount(): Int {
        var groupCounter = 0
        for (element in fileSnapshots) {
            if (element.group > groupCounter)
                groupCounter = element.group
        }
        return groupCounter
    }

    fun getLayerCount(group: Int): Int {
        var fileSnapshotsThisGroup = ArrayList<FileSnapshot>()
        for (element in fileSnapshots) {
            if (element.group == group)
                fileSnapshotsThisGroup.add(element)

        }
        var layerCounter = 0
        for (element in fileSnapshotsThisGroup) {
            if (element.layer > layerCounter)
                layerCounter = element.layer
        }
        return layerCounter
    }

    fun getRoundRobinCount(group: Int, layer: Int): Int {
        var fileSnapshotsThisGroupAndLayer = ArrayList<FileSnapshot>()
        for (element in fileSnapshots) {
            if (element.group == group && element.layer == layer)
                fileSnapshotsThisGroupAndLayer.add(element)
        }
        var getRoundRobinCounter = 0
        for (element in fileSnapshotsThisGroupAndLayer) {
            if (element.roundRobin > getRoundRobinCounter)
                getRoundRobinCounter = element.roundRobin
        }
        return getRoundRobinCounter
    }

    fun getAssetFileDescriptor(
        group: Int,
        layer: Int,
        roundRobin: Int
    ): AssetFileDescriptor? {
        for (element in fileSnapshots) {
            if (element.group == group &&
                element.layer == layer &&
                element.roundRobin == roundRobin &&
                element.libraryName == libraryName
            )//compiler told me this is fine, but I'm not sure...not .equals? or .contains?
                return element.assetFileDescriptor
        }
        return null
    }

    fun getFileSnapshot(groupIteration: Int, layerIteration: Int, roundRobinIteration: Int): FileSnapshot? {
        var toReturn: FileSnapshot? = null
        for (element in fileSnapshots) {
            if(
                element.group == groupIteration &&
                element.layer == layerIteration &&
                element.roundRobin == roundRobinIteration){
                toReturn = element
                break
            }
        }
        return toReturn
    }
}

data class FileSnapshot(
    val index: Int,
    val fileName: String,
    val group: Int,
    val layer: Int,
    val roundRobin: Int,
    val libraryName: String,
    val assetFileDescriptor: AssetFileDescriptor,
    var assetManager: AssetManager
) {
    fun equals(toFind: FileSnapshot): Boolean {
        return toFind.group == group &&
                toFind.layer == layer &&
                toFind.roundRobin == roundRobin &&
                toFind.libraryName.contains(libraryName)//compiler told me this is fine, but I'm not sure...not .equals? or .contains?
    }
}
