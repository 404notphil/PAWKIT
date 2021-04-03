package com.tunepruner.fingerperc.launchscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.R

class LibraryListFragment : Fragment() {
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private val TAG = "LibraryListFragment.Class"
    private lateinit var viewModel: LibraryNameViewModel

    companion object {
        fun newInstance(): Fragment {
            val libraryListFragment = LibraryListFragment()
            return libraryListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        val view: View = inflater.inflate(R.layout.launch_screen, container, false)
        imageView1 = view.findViewById(R.id.bombo_button)
        imageView2 = view.findViewById(R.id.cajon_button)
        setButtonHandlers()
        return view
    }
    //Todo review the first chapter of this course (https://www.linkedin.com/learning/android-development-essential-training-manage-data-with-kotlin/share-data-with-livedata-objects-2?contextUrn=urn%3Ali%3AlyndaLearningPath%3A5a724cba498e9ec2d506035e)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LibraryNameViewModel::class.java)
        viewModel.libraryNameData.observe(
            viewLifecycleOwner,
            {//TOdo Gassner passed in "this" as the first parameter, but the compiler won't let me! Figure out why, or find out if this works!
                for (element in it) {
                    Log.i(
                        TAG,
                        "library name = $element.libraryName (already installed = $element.currentlyInstalled)"
                    )
                }
            })
        // TODO: Use the ViewModel
    }

    private fun setButtonHandlers() {
        Log.i(tag, "${activity == null}")
        imageView1?.setOnClickListener {
            Log.i(tag, "Testing")
            fadeOut(imageView1)
            val intent = Intent(activity, InstrumentActivity::class.java).apply {
                putExtra("libraryName", "cajon")
            }
            startActivity(intent)
        }

        imageView2?.setOnClickListener {
            fadeOut(imageView2)
            val intent = Intent(activity, InstrumentActivity::class.java).apply {
                putExtra("libraryName", "dancedrums")
            }
            startActivity(intent)
        }
    }


    private fun fadeOut(viewToFadeOut: View?) {
        val fadeOut: Animation = AlphaAnimation(1F, 0F)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 10

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                viewToFadeOut?.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationStart(animation: Animation?) {}
        })
        viewToFadeOut?.startAnimation(fadeOut)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        imageView1 = activity?.findViewById(R.id.cajon_button)
        imageView2 = activity?.findViewById(R.id.bombo_button)
        Log.i(tag, "${imageView1 == null}")
        setButtonHandlers()
    }
}