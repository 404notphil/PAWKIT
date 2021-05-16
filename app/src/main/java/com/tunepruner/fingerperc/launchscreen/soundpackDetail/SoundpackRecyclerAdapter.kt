package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryDetails
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerAdapter
import com.tunepruner.fingerperc.launchscreen.librarylist.SoundpackDetails

class SoundpackRecyclerAdapter(
    context: Context,
    libraries: ArrayList<LibraryDetails>,
    soundpacks: ArrayList<SoundpackDetails>,
    mLibraryItemListener: LibraryItemListener,
    private val currentSoundpack: String
) : LibraryListRecyclerAdapter(context, libraries, soundpacks, mLibraryItemListener) {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.soundpack_grid_item, parent, false)
    }


}