package com.tunepruner.bomboleguerodemo.graphics

class AnimationManager {
    //get trigger that was pressed
    //a val property to store its zone
    //a var property to store its layer, and decrement for each cycle
    //a var property called interrupt, set to false
    //a function called interrupt() that can set the property to true
    //a property called zones which is a List<List<Cycle>>
        //each List<Cycle>  contains a Cycle for each layer in that zone
    //an init function that calls ResourceManager to get info on zones and layers and populates each List<Cycle> in zones
        //it creates an array of four cycles (because there are four image sets)
            //and it decides how to pass those out to the layers.
    //a Cycle object, which receives a different pair of resources for each instance
        //has a function called cycle()
    //a function which receives the trigger zone
        //resets all properties
        //based on layer level, it
}