//package com.tunepruner.fingerperc.launchscreen.librarydetail
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.tunepruner.fingerperc.R
//import android.support.v4.*
//import androidx.fragment.app.Fragment
//import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerFragment
//
//class LibraryDetailFragment : Fragment() {
//    private lateinit var mListener: LibraryListRecyclerFragment.FragmentListener
//    //    private lateinit var viewModel: LibraryDetailViewModel
//    private val TAG = "LibraryListFragment.Class"
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view: View = inflater.inflate(R.layout.library_detail_fragment, container, false)
//        return view
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//    }
//    override fun onAttachFragment(childFragment: Fragment) {
//        super.onAttachFragment(childFragment)
//
//        if (context is LibraryListRecyclerFragment.FragmentListener) mListener =
//            context as LibraryListRecyclerFragment.FragmentListener
//
//
//    }
//
//    interface FragmentListener {
//        fun onFragmentFinished()
//    }
//}
