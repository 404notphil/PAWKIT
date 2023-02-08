<h1 align="center">PAWKIT</h1>
<p align="center">
PAWKIT is a drum instrument in the form of a mobile app, packaged with a collection of custom sampled acoustic instruments. Through a custom triggering interface, it provides a highly dynamic way to trigger sampled drum sounds. <br><br></p>

 <p align="center">
<a href="https://play.google.com/store/apps/details?id=com.tunepruner.fingerperc&hl=en_US&gl=US" target="_blank"><img src="google-play-badge.png" width="200" alt="Screen recording of app"></a>
</p>
<br><br>
 <p align="center">
 <img src="gifOfDemo.gif" width="200" alt="Screen recording of app">

_____


# Abstract

PAWKIT is a dynamic, zone-based percussion interface,
packaged with deep-sampled acoustic percussion instruments and drum kits. A user can load an instrument and then
create sound by tapping the screen. For each instrument, there are two zones on the screen which represent the
primary sounds that the instrument can make. For example, in the case of the Bombo Legüero,
those two sounds are 1) a hit on the wooden rim of the drum and 2) a hit in the center of the "drum head", 
or membrane. In the case of a drum kit, the two zones correspond to the snare drum and the kick drum.
The the volume of each drum hit is determined by where you tap on the Y axis.
[You can watch my demonstration video to see it in action.](https://youtu.be/RQXtMMSJ8G4)


# Under the hood

PAWKIT installs with hundreds of audio samples which are
individually triggered when a user touches various zones on the screen. As mentioned above,
each instrument has two principal sounds, often referred to as "articulations", and each corresponding
to a zone which occupies half of the device's screen. Within each articulation zone, there are many
subzones, generated automatically based on the provided sample set, and those each correspond to a
different volume for that articulation. Volume levels are more precisely referred to as "Velocities" hereafter.
While velocity level is the deepest level of detail that the user can control, there is another
layer of depth to the sample set. That layer is made of groups of recorded audio samples
where I played the drum at an identical volume, but allowed human imperfection.
These repetitions are called "Round Robins" in the audio sample industry, and
I recorded up to 10 of them for each velocity layer.
When a user triggers the exact same zone multiple times in a row, programming logic
chooses any round robin from those available, excluding the last one played, and submits its choice to the Player service.
The reason that RoundRobins are used is to avoid the dreaded "machine gun effect" that happens in
virtual instruments of lower quality. [You can watch my technical walkthrough video for more info.](https://youtu.be/6Hf7qcKE2H8)

# Status

PAWKIT is currently in beta.

# Tech used/dependencies
- In-app updates
- In-app purchases
- Firebase
- Google Analytics
- [Google Oboe](https://github.com/google/oboe)
- CMake
- JNI
- JUnit
- Mockito
- PlantUML for docs
- [YouTube library by PierFranciscoSoffriti](https://github.com/PierfrancescoSoffritti/android-youtube-player)

# Architecture

[On this page, ](./app/Documentation/RenderedImages/RenderedImages.md) I've provided some 
notes and diagrams that describe how the whole project works, and it should
greatly help anyone who wants to dig in.
