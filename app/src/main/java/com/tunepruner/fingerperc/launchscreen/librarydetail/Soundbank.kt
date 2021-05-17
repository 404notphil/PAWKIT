package com.tunepruner.fingerperc.launchscreen.librarydetail


class Soundbank(val libraries: ArrayList<Library>, val soundpacks: ArrayList<Soundpack>) {
    private val checkTypeFunctions = HashMap<CheckType, (library: Library) -> Boolean>()
    private val setTypeFunctions = HashMap<SetType, (value: Boolean, library: Library) -> Unit>()


    init {
        checkTypeFunctions[CheckType.IS_RELEASED] = { getSoundpack(it)?.isReleased ?: false }
        checkTypeFunctions[CheckType.IS_PURCHASED] = { getSoundpack(it)?.isPurchased ?: false }
        checkTypeFunctions[CheckType.IS_INSTALLED] = { getSoundpack(it)?.isInstalled ?: false }
        setTypeFunctions[SetType.IS_RELEASED] = { value, library ->  getSoundpack(library)?.isReleased = value }
        setTypeFunctions[SetType.IS_PURCHASED] = {  value, library ->  getSoundpack(library)?.isPurchased = value }
        setTypeFunctions[SetType.IS_INSTALLED] = {  value, library ->  getSoundpack(library)?.isInstalled = value }
    }

    fun check(checkType: CheckType, library: Library): Boolean {
        val result = checkTypeFunctions[checkType]?.invoke(library)
        return result ?: false
    }

    fun set(setType: SetType, value: Boolean, library: Library) {
        setTypeFunctions[setType]?.invoke(value, library)
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

    enum class SetType {
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