package com.tunepruner.fingerperc.launchscreen.librarylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundbank

open class LibraryListRecyclerAdapter(
    open val context: Context,
    val soundbank: Soundbank,
    private val mLibraryItemListener: LibraryItemListener
) :
    RecyclerView.Adapter<LibraryListRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerButtonTitle: TextView = itemView.findViewById(R.id.recycler_button_title)
        val recyclerButtonSubtitle: TextView = itemView.findViewById(R.id.recycler_button_sub_title)
        val recyclerButtonImage: ImageView = itemView.findViewById(R.id.recycler_button_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflate(inflater, parent)
        return ViewHolder(view)
    }

    open fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.library_grid_item, parent, false)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val library = soundbank.libraries[position]
        with(holder) {
            recyclerButtonTitle.let {
                it.text = library.libraryName
                it.contentDescription = library.libraryName
            }
            recyclerButtonSubtitle.let {
                if (soundbank.check(Soundbank.CheckType.IS_RELEASED, library))
                    if (soundbank.check(Soundbank.CheckType.IS_PURCHASED, library))
                        if (soundbank.check(Soundbank.CheckType.IS_INSTALLED, library))
                            it.text = "Tap to play!"
                        else it.text = "Tap to install"
                    else it.text = "Check it out!"
                else it.text = "Coming soon!"
            }
//            val picasso = Picasso.get()
            recyclerButtonImage.transitionName = "${library.soundpackID}"

            Glide.with(context)
                .load(library.imageUrl)
                .into(recyclerButtonImage)

            itemView.setOnClickListener {
                mLibraryItemListener.onLibraryItemClick(library, recyclerButtonSubtitle)
            }
        }

        holder.itemView.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null)
        }
    }

    override fun getItemCount(): Int {
        return soundbank.libraries.size
    }

    interface LibraryItemListener {
        fun onLibraryItemClick(
            library: Library,
            recyclerButtonSubtitle: TextView
        )
    }
}