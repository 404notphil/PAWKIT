package com.tunepruner.fingerperc.launchscreen.librarydetail


class Soundbank(val libraries: ArrayList<Library>, val soundpacks: ArrayList<Soundpack>){

}

data class Library(
    val index: Int? = null,
    val libraryName: String? = null,
    val imageUrl: String? = null,
    @field:JvmField
    var isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val libraryID: String? = null,
    val soundpackID: String? = null,
    val soundpackName: String? = null
)
//changing this class might ruin firebase connection. Needs no-arguments constructor. But order of arguments doesn't matter

data class Soundpack(
    val soundpackID: String? = null,
    val soundpackName: String? = null,
    val index: Int? = null,
    val thumbnailImageUrl: String? = null,
    val titleImageUrl: String? = null,
    @field:JvmField
    var isPurchased: Boolean? = null,
    @field:JvmField
    var isInstalled: Boolean? = null,
    @field:JvmField
    var isReleased: Boolean? = null,
    val members: ArrayList<Library> = ArrayList(),
){

}