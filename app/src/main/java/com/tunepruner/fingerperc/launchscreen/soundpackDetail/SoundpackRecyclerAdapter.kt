package com.tunepruner.fingerperc.launchscreen.soundpackDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryDetails

class SoundpackListRecyclerAdapter (
    val context: Context,
    private val libraries: List<LibraryDetails>,
    private val mSoundpackItemListener: SoundpackItemListener
) :
    RecyclerView.Adapter<SoundpackListRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerButtonTitle: TextView = itemView.findViewById(R.id.recycler_button_title)
        val recyclerButtonSubtitle: TextView = itemView.findViewById(R.id.recycler_button_sub_title)
        val recyclerButtonImage: ImageView = itemView.findViewById(R.id.recycler_button_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.library_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val library = libraries[position]
        with(holder) {
            recyclerButtonTitle.let {
                it.text = library.libraryName
                it.contentDescription = library.libraryName
            }
            recyclerButtonSubtitle.let {
                if (library.isReleased == true)
                    if (library.isPurchased == true)
                        if (library.isInstalled == true)
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
//            recyclerButtonImage.setImageResource(R.mipmap.head_png_foreground)

            itemView.setOnClickListener {
                mSoundpackItemListener.onLibraryItemClick(library, recyclerButtonSubtitle)
            }
        }
    }

    override fun getItemCount(): Int {
        return libraries.size
    }

    interface SoundpackItemListener {
        fun onSoundpackItemClick(
            libraryDetails: SoundpackDetails,
            recyclerButtonSubtitle: TextView
        )
    }
}