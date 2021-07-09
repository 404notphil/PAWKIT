package com.tunepruner.fingerperc.launchscreen.librarylist

import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarydetail.Soundbank
import java.io.File
import java.text.DecimalFormat

class LibraryListRecyclerFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener {
    private val TAG = "LibraryListRecyclerFragment.Class"
    private val wasSet = false
    private val viewModel: SoundbankViewModel by viewModels {
        SoundbankViewModelFactory(
            requireActivity().application,
            ""
        )
    }
    private lateinit var mListener: FragmentListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (context is FragmentListener) mListener = context as FragmentListener
        val view: View = inflater.inflate(R.layout.launch_screen2, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(SpacesItemDecoration(50))
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        observeLiveData()

        return view
    }

    //Todo review the first chapter of this course (https://www.linkedin.com/learning/android-development-essential-training-manage-data-with-kotlin/share-data-with-livedata-objects-2?contextUrn=urn%3Ali%3AlyndaLearningPath%3A5a724cba498e9ec2d506035e)

    override fun onResume() {
        super.onResume()
        observeLiveData()
        setupDeveloperContactButton()
    }

    private fun observeLiveData() {
        viewModel.soundbank.observe(viewLifecycleOwner) { soundbank ->
            val adapter = LibraryListRecyclerAdapter(
                requireContext(),
                soundbank,
                this
            )
            recyclerView.adapter = adapter

        }
    }


    override fun onLibraryItemClick(
        library: Library,
        recyclerButtonSubtitle: TextView
    ) {
        val interval = 10
        val amountOfIntervals = 10
        val duration = interval * amountOfIntervals
        when {
            viewModel.soundbank.value?.check(Soundbank.CheckType.IS_RELEASED, library) == false ||
                    viewModel.soundbank.value?.check(Soundbank.CheckType.IS_RELEASED, library) == null -> {
//                navController.navigate(R.id.comingSoonFragment)
                val text = "Coming soon!"
                val toastDuration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, toastDuration)
                toast.show() }
            viewModel.soundbank.value?.check(Soundbank.CheckType.IS_INSTALLED, library) == false &&
                    viewModel.soundbank.value?.check(Soundbank.CheckType.IS_PURCHASED, library) == true -> {
                val intent = Intent(requireActivity(), UpdateDialogActivity::class.java)
                startActivity(intent)
            }
            else -> {
                val action =
                    LibraryListRecyclerFragmentDirections.actionLaunchScreenFragmentToLibraryDetailFragment3(
                        library.libraryName ?: "",
                        library.libraryID ?: "",
                        library.soundpackID ?: "",
                        library.imageUrl ?: "",
                        viewModel.soundbank.value?.check(Soundbank.CheckType.IS_PURCHASED, library)?: false,
                        "$0.99",
                        library.soundpackName ?: "(unknown name)"
                    )
                val recyclerButtonImage =
                    requireActivity().findViewById<ImageView>(R.id.recycler_button_image)
                val extras =
                    FragmentNavigatorExtras(recyclerButtonImage to "${library.soundpackID}")
                navController.navigate(action, extras)
            }
        }
    }

    private fun setupDeveloperContactButton() {
        val file = File(requireActivity().application.filesDir, "is_beta")
        val textFromFile: String = if (file.exists()) {
            file.readText()
        } else "null"
        var isBeta = when {/*this will potentially be changed by the database check*/
            textFromFile.contains("true") -> true
            textFromFile.contains("false") -> false
            else -> null
        }
        if (isBeta == false) {
            val buttonParent = requireActivity().findViewById<LinearLayout>(R.id.linearLayout2)
            val button = requireActivity().findViewById<Button>(R.id.send_phil_feedback)
            buttonParent.removeView(button)
        } else {
            //get device memory
            val actManager: ActivityManager =
                requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)

            //setup all the substrings for email
            val separator = "\n________________________________\n"
            val answer = "\n▼\n\n\n\n▲$separator"
            val androidVersion = "Android version: ${android.os.Build.VERSION.SDK_INT}"
            val modelVersion = "Phone model: ${capitalize(getDeviceName() ?: "")}"
            val totalRAM = "Total RAM: ${bytesToHuman(memInfo.totalMem.toDouble())}"
            val availableRam = "RAM available: ${bytesToHuman(memInfo.availMem.toDouble())}"
            val bluetoothConnected = "Bluetooth connected: ${isBluetoothHeadsetConnected()}"
            val questionQuitUnexp = "How many times has PAWKIT closed unexpectedly for you?"
            val questionSound = "Did the instruments sound clear, like in the demo video?"
            val otherProblems = "Has anything else gone wrong in the app?"
            val anySuggestions = "Any suggestions about the design, layout, or anything else?"

            requireActivity().findViewById<Button>(R.id.send_phil_feedback).setOnClickListener {
                val i = Intent(Intent.ACTION_SENDTO)
                i.data = Uri.parse("mailto:")
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf("philcarlson.developer@gmail.com"))
                i.putExtra(Intent.EXTRA_SUBJECT, "*TesterFeedback*")
                i.putExtra(
                    Intent.EXTRA_TEXT,
                    "" +
                            "$separator" +
                            "$questionQuitUnexp$answer" +
                            "$questionSound$answer" +
                            "$otherProblems$answer" +
                            "$anySuggestions$answer" +
                            "THANK YOU!!! : D" +
                            "$separator" +
                            "$separator" +
                            "($androidVersion\n" +
                            "$modelVersion\n" +
                            "$totalRAM\n" +
                            "$availableRam\n" +
                            "$bluetoothConnected)" +
                            "$separator" +
                            "$separator"
                )
                try {
                    startActivity(Intent.createChooser(i, "Email to developer..."))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(
                        requireActivity(),
                        "There are no email clients installed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun bytesToHuman(size: Double): String? {
        val Kb: Double= 1024.0
        val Mb: Double = Kb * 1024.0
        val Gb: Double = Mb * 1024.0
        val Tb: Double = Gb * 1024.0
        val Pb: Double = Tb * 1024.0
        val Eb: Double = Pb * 1024.0
        val f = DecimalFormat("##.00")

        if (size < Kb) return "$size byte"
        if (size > Kb && size < Mb ) return f.format(size / Kb).toString() + " KB"
        if (size > Mb && size < Gb ) return f.format(size / Mb).toString() + " MB"
        if (size > Gb && size < Tb ) return f.format(size / Gb).toString() + " GB"
        if (size > Tb && size < Pb ) return f.format(size / Tb).toString() + " TB"
        if (size > Pb && size < Eb ) return f.format(size / Pb).toString() + " Pb"
        return if (size >= Eb) ((size / Eb)).toString() + " Eb" else "0"
    }

    fun getDeviceName(): String? {
        val manufacturer: String = Build.MANUFACTURER
        val model: String = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

    fun isBluetoothHeadsetConnected(): Boolean {
        val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) === BluetoothHeadset.STATE_CONNECTED)
    }

    interface FragmentListener {
        fun onFragmentFinished(library: Library)
    }
}
