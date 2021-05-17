package com.tunepruner.fingerperc.launchscreen.librarydetail


class Soundbank(val libraries: ArrayList<Library>, val soundpacks: ArrayList<Soundpack>) {
    private val functions = HashMap<CheckType, (library: Library) -> Boolean>()

    init {
        functions[CheckType.IS_RELEASED] = { getSoundpack(it)?.isReleased ?: false }
        functions[CheckType.IS_PURCHASED] = { getSoundpack(it)?.isPurchased ?: false }
        functions[CheckType.IS_INSTALLED] = { getSoundpack(it)?.isInstalled ?: false }
    }

    fun check(checkType: CheckType, library: Library): Boolean {
        val result = functions[checkType]?.invoke(library)
        return result ?: false
    }

    private fun getSoundpack(library: Library): Soundpack?{
        for (soundpack in soundpacks) {
            for (member in soundpack.members) {
                if (member.soundpackID == library.soundpackID) {
                    return soundpack
                }
            }
        }
        return null
    }

    enum class CheckType {
        IS_RELEASED,
        IS_PURCHASED,
        IS_INSTALLED
    }
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
)