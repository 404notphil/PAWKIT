**Finger Perc**
=======
Android app

*Possibly the only percussion instrument for touchscreen that allows you to control dynamics*

Screen recording of app: 

<img src="gifOfDemo.gif" width="200" alt="Screen recording of app">

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
each instrument has two principal sounds, often referred to as "articulations", and each of those
corresponds to a zone (occupying half of the device's screen). Each of those two zones has many 
subzones, generated automatically based on the provided sample set. 
Those sub-zone correspond to a different volume for that articulation. 
(Volume levels are more precisely referred to as "Velocities" hereafter)
While velocity level is the deepest level of detail that the user can control, there is another 
layer of depth to the sample set. That layer is made of groups of recorded audio samples 
where I "attepmpted" to play the drum at an identical volume, but allowing human imperfection. 
These repetitions are called "Round Robins" in the audio sample industry, and 
I recorded up to 10 of them for each velocity layer.
When a user triggers the exact same zone multiple times in a row, programming logic
chooses any sample except for the last one played and submits that to the Player service.
The reason that RoundRobins are used is to avoiding the dreaded "machine gun effect" that happens in 
virtual instruments of lower quality. 

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
