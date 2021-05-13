package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryDetails
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerAdapter

class SoundpackRecyclerAdapter(
    override val context: Context,
    private val libraries: List<LibraryDetails>,
    private val mLibraryItemListener: LibraryItemListener
) : LibraryListRecyclerAdapter(context, libraries, mLibraryItemListener) {

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.soundpack_grid_item, parent, false)
    }

}