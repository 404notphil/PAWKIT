package com.tunepruner.fingerperc.graphics

import com.tunepruner.fingerperc.zone.zonegraph.articulationzone.velocityzone.VelocityZone

interface GUIManager {

    //get trigger that was pressed
    //a val property to store its zone
    //a val property to store its layer
    //a var property called restart, set to false
    //a function called restart() that can set the property to true
    //a property called zones which is a List<List<Cycle>>
        //each List<Cycle>  contains a Cycle for each layer in that zone
    //an init function that calls ResourceManager to get info on zones and layers and populates each List<Cycle> in zones
        //it creates an array of four cycles (because there are four image sets)
            //and it decides how to pass those out to the layers.
    //a Cycle object
        //receives a different pair of resources for each instance in val
        //receives a Cycle?
        //has a function called cycle() which calls another cycle method unless null
    //a function called startAnimation() which receives the trigger zone
        //resets all properties
        //based on layer level, it accesses zones and calls cycle on appropriate Cycle object
    fun startAnimation(velocityZone: VelocityZone)

}