<p align="center">

  <b><font size="+20">FingerPerc</b><br>

   A touchscreen percussion instrument that can <em>actually</em> make expressive music.<br>
  <a Android app </a>
  <img src="gifOfDemo.gif" width="200" alt="Screen recording of app">
</p>




_____

Screen recording of app: 



# Abstract

This app is a musical instrument. The sounds it makes correspond to two different percussion 
instruments: 1) Bombo Legüero, a traditional Andean folk music drum typically made of 
wood and alpaca skins or cowhide 2) Cajón Flamenco, the popular Spanish variation of a box drum,
containing snares. A user can play these sounds by tapping the screen, and the dynamic level 
(the volume, basically) is determined by where you tap. For each instrument, there are two zones 
on the screen which represent the primary sounds that the instrument can make. In the case of the 
Bombo Legüero, those two sounds are 1) a hit on the wooden rim of the drum and 2) a hit in the 
center of the drum head (membrane, that is). With the Cajón Flamenco, the two sounds are 1) a 
hit on the top edge and 2) a hit in the front center. 


# Under the hood
FingerPerc installs with hundreds of audio samples which are 
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
chooses any RoundRobin, excluding the last one played, and submits its choice to the Player service.
The reason that RoundRobins are used is to avoid the dreaded "machine gun effect" that happens in 
virtual instruments of lower quality. 

# Status

The first test build of the app is complete and has been released for internal testing. 

# Tech used

- Gradle
- [Google Oboe](https://github.com/google/oboe) 
- CMake
- JNI
- JUnit
- Mockito
- PlantUML for docs

# Architecture

[On this page, ](./app/Documentation/RenderedImages/RenderedImages.md) I've provided some 
notes and diagrams that describe how the whole project works in the abstract, and it should 
greatly help anyone who wants to dig in.  
