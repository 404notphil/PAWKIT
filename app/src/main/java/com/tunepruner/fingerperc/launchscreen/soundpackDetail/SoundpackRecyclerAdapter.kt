package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundbank
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerAdapter

class SoundpackRecyclerAdapter(
    context: Context,
    soundbank: Soundbank,
    mLibraryItemListener: LibraryItemListener,
    private val currentSoundpack: String
) : LibraryListRecyclerAdapter(context, soundbank, mLibraryItemListener) {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.soundpack_grid_item, parent, false)
    }
}