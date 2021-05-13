package com.tunepruner.fingerperc.instrument

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.util.Log

class ResourceManager(context: Context, private val libraryName: String) {
    private var counter: Int = 0
    val fileSnapshots = ArrayList<FileSnapshot>()


    init {
        analyzeFiles(context)
    }

    private fun analyzeFiles(context: Context) {

        val assetManager: AssetManager = context.assets
        val filePaths = assetManager.list("audio")
            ?: error("AssetManager couldn't get filePaths")
        val filePathsFiltered = ArrayList<String>()
        for (element in filePaths) {
            if (element.contains(libraryName)) filePathsFiltered.add(element)
        }
        for (element in filePathsFiltered) {
            val filename = "audio/${element}"
            val afd: AssetFileDescriptor =
                assetManager.openFd(filename)
            val fileSnapshot = filenameToSnapshot(filename, afd, assetManager)
            fileSnapshots.add(fileSnapshot)
//            Log.i("ResourceManager = ", element)
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

    fun getArticulationCount(): Int {
        var articulationCounter = 0
        for (element in fileSnapshots) {
            if (element.articulationNumber > articulationCounter)
                articulationCounter = element.articulationNumber
        }
        return articulationCounter
    }

    fun getVelocityLayerCount(articulationNumber: Int): Int {
        val fileSnapshotsThisArticulation = ArrayList<FileSnapshot>()
        for (element in fileSnapshots) {
            if (element.articulationNumber == articulationNumber)
                fileSnapshotsThisArticulation.add(element)

        }
        var layerCounter = 0
        for (element in fileSnapshotsThisArticulation) {
            if (element.velocityNumber > layerCounter)
                layerCounter = element.velocityNumber
        }
        return layerCounter
    }

    fun getRoundRobinCount(articulationNumber: Int, velocityNumber: Int): Int {
        val fileSnapshotsThisArticulationAndVelocity = ArrayList<FileSnapshot>()
        for (element in fileSnapshots) {
            if (element.articulationNumber == articulationNumber && element.velocityNumber == velocityNumber)
                fileSnapshotsThisArticulationAndVelocity.add(element)
        }
        var getRoundRobinCounter = 0
        for (element in fileSnapshotsThisArticulationAndVelocity) {
            if (element.roundRobinNumber > getRoundRobinCounter)
                getRoundRobinCounter = element.roundRobinNumber
        }
        return getRoundRobinCounter
    }

    fun getAssetFileDescriptor(
        articulationNumber: Int,
        velocityNumber: Int,
        roundRobinNumber: Int
    ): AssetFileDescriptor? {
        for (element in fileSnapshots) {
            if (element.articulationNumber == articulationNumber &&
                element.velocityNumber == velocityNumber &&
                element.roundRobinNumber == roundRobinNumber
            )//Library name isn't needed here, because fileSnapshots already filtered samples from other libraries
                return element.assetFileDescriptor
        }
        return null
    }

    fun getFileSnapshot(articulationNumber: Int, velocityNumber: Int, roundRobinNumber: Int): FileSnapshot? {
        var toReturn: FileSnapshot? = null
        for (element in fileSnapshots) {
            if(
                element.articulationNumber == articulationNumber &&
                element.velocityNumber == velocityNumber &&
                element.roundRobinNumber == roundRobinNumber){
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
    val articulationNumber: Int,
    val velocityNumber: Int,
    val roundRobinNumber: Int,
    val libraryName: String,
    val assetFileDescriptor: AssetFileDescriptor,
    var assetManager: AssetManager
) {
    fun equals(toFind: FileSnapshot): Boolean {
        return toFind.articulationNumber == articulationNumber &&
                toFind.velocityNumber == velocityNumber &&
                toFind.roundRobinNumber == roundRobinNumber &&
                toFind.libraryName.contains(libraryName)//compiler told me this is fine, but I'm not sure...not .equals? or .contains?
    }
}
