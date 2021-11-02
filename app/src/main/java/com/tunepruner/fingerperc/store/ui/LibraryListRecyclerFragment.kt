package com.tunepruner.fingerperc.store.ui

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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.tunepruner.fingerperc.BuildConfig
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.databinding.LaunchScreen2Binding
import com.tunepruner.fingerperc.store.Library
import com.tunepruner.fingerperc.store.Soundbank
import com.tunepruner.fingerperc.store.ui.util.SpacesItemDecoration
import com.tunepruner.fingerperc.store.viewmodel.SoundbankViewModel
import com.tunepruner.fingerperc.store.viewmodel.SoundbankViewModelFactory
import java.io.File
import java.text.DecimalFormat

class LibraryListRecyclerFragment : Fragment(), LibraryListRecyclerAdapter.LibraryItemListener {
    private val wasSet = false
    private val viewModel: SoundbankViewModel by viewModels {
        SoundbankViewModelFactory(
            requireActivity().application,
            ""
        )
    }
    private lateinit var mListener: FragmentListener
    private lateinit var navController: NavController
    private lateinit var binding: LaunchScreen2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LaunchScreen2Binding.inflate(layoutInflater)

        if (context is FragmentListener) mListener = context as FragmentListener
        binding.recyclerView.addItemDecoration(SpacesItemDecoration(50))
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        return binding.root
    }

    //Todo review the first chapter of this course (https://www.linkedin.com/learning/android-development-essential-training-manage-data-with-kotlin/share-data-with-livedata-objects-2?contextUrn=urn%3Ali%3AlyndaLearningPath%3A5a724cba498e9ec2d506035e)

    override fun onResume() {
        super.onResume()
        observeLiveData()
        viewModel.getData()
        setupDeveloperContactButton()
    }

    private fun observeLiveData() {
        viewModel.soundbankLiveData.observe(viewLifecycleOwner) { soundbank ->
            Log.i("log_tag", "observeLiveData")
            val adapter = LibraryListRecyclerAdapter(
                requireContext(),
                soundbank,
                this
            )

            binding.recyclerView.adapter = adapter
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
            viewModel.soundbankLiveData.value?.check(
                Soundbank.CheckType.IS_RELEASED,
                library
            ) == false ||
                    viewModel.soundbankLiveData.value?.check(
                        Soundbank.CheckType.IS_RELEASED,
                        library
                    ) == null -> {
//                navController.navigate(R.id.comingSoonFragment)
                val text = "Coming soon!"
                val toastDuration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(context, text, toastDuration)
                toast.show()
            }
            viewModel.soundbankLiveData.value?.check(
                Soundbank.CheckType.IS_INSTALLED,
                library
            ) == false &&
                    viewModel.soundbankLiveData.value?.check(
                        Soundbank.CheckType.IS_PURCHASED,
                        library
                    ) == true -> {
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
                        viewModel.soundbankLiveData.value?.check(
                            Soundbank.CheckType.IS_PURCHASED,
                            library
                        ) ?: false,
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
        val isBeta = when {/*this will potentially be changed by the database check*/
            textFromFile.contains("true") -> true
            textFromFile.contains("false") -> false
            else -> null
        }
        if (isBeta == false) {
            val buttonParent = binding.linearLayout2
            val button = binding.sendPhilFeedback
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
            val androidVersion = "Android version: ${Build.VERSION.SDK_INT}"
            val modelVersion = "Phone model: ${capitalize(getDeviceName() ?: "")}"
            val totalRAM = "RAM used: ${bytesToHuman(memInfo.totalMem.toDouble())}"
            val availableRam = "RAM available: ${bytesToHuman(memInfo.availMem.toDouble())}"
            val bluetoothConnected = "Bluetooth connected: ${isBluetoothHeadsetConnected()}"
            val questionQuitUnexp = "How many times has PAWKIT closed unexpectedly for you?"
            val questionSound = "Did the instruments sound clear, like in the demo video?"
            val otherProblems = "Has anything else gone wrong in the app?"
            val anySuggestions = "Any suggestions about the design, layout, or anything else?"

            requireActivity().findViewById<Button>(R.id.send_phil_feedback).setOnClickListener {
                val i = Intent(Intent.ACTION_SENDTO)
                i.data = Uri.parse("mailto:")
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.DEVELOPER_EMAIL))
                i.putExtra(Intent.EXTRA_SUBJECT, "*TesterFeedback*")
                i.putExtra(
                    Intent.EXTRA_TEXT,
                    "" +
                            separator +
                            "$questionQuitUnexp$answer" +
                            "$questionSound$answer" +
                            "$otherProblems$answer" +
                            "$anySuggestions$answer" +
                            "THANK YOU!!! : D" +
                            separator +
                            separator +
                            "($androidVersion\n" +
                            "$modelVersion\n" +
                            "$totalRAM\n" +
                            "$availableRam\n" +
                            "$bluetoothConnected)" +
                            separator +
                            separator
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
        val kB = 1024.0
        val mB: Double = kB * 1024.0
        val gB: Double = mB * 1024.0
        val tB: Double = gB * 1024.0
        val pB: Double = tB * 1024.0
        val eB: Double = pB * 1024.0
        val f = DecimalFormat("##.00")

        if (size < kB) return "$size byte"
        if (size > kB && size < mB) return f.format(size / kB).toString() + " KB"
        if (size > mB && size < gB) return f.format(size / mB).toString() + " MB"
        if (size > gB && size < tB) return f.format(size / gB).toString() + " GB"
        if (size > tB && size < pB) return f.format(size / tB).toString() + " TB"
        if (size > pB && size < eB) return f.format(size / pB).toString() + " Pb"
        return if (size >= eB) ((size / eB)).toString() + " Eb" else "0"
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

    private fun isBluetoothHeadsetConnected(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter() ?: null
        return (bluetoothAdapter?.isEnabled ?: false && bluetoothAdapter?.getProfileConnectionState(
            BluetoothHeadset.HEADSET
        ) === BluetoothHeadset.STATE_CONNECTED)
    }

    interface FragmentListener {
        fun onFragmentFinished(library: Library)
    }
}