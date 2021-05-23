package com.tunepruner.fingerperc.launchscreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.tunepruner.fingerperc.InstrumentActivity
import com.tunepruner.fingerperc.R
import com.tunepruner.fingerperc.launchscreen.librarydetail.Library
import com.tunepruner.fingerperc.launchscreen.librarylist.LibraryListRecyclerFragment
import java.util.*

class LaunchScreenActivity : AppCompatActivity(), LibraryListRecyclerFragment.FragmentListener {
    private val TAG = "LaunchScreenActivity.Class"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebaseAnalytics = FirebaseAnalytics.getInstance(application)
        firebaseAnalytics.setUserProperty(
            "android_version",
            android.os.Build.VERSION.SDK_INT.toString()
        )

        System.loadLibrary("bomboleguero")//TODO this is the JNI one, and shouldn't use the libraryName string. It should be refactored eventually!

        actionBar?.hide()
        setContentView(R.layout.main_activity_testing_navhost)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


    }

    override fun onResume() {
        super.onResume()
        System.loadLibrary("bomboleguero")
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        val separator = "\n________________________________\n"
        val answer = "\n-->\n\n$separator"
        val androidVersion = "Android version: ${android.os.Build.VERSION.SDK_INT}"
        val modelVersion = "Phone model: ${capitalize(getDeviceName()?: "")}"
        val questionQuitUnexp = "How many times has PAWKIT closed unexpectedly for you?"
        val questionSound = "Did the instruments sound clear, like in the demo video?"
        val otherProblems = "Has anything else gone wrong in the app?"
        val anySuggestions = "Any suggestions about the design, layout, or anything else?"

        findViewById<Button>(R.id.send_phil_feedback).setOnClickListener{
            val i = Intent(Intent.ACTION_SENDTO)
            i.data = Uri.parse("mailto:");
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("philcarlson.developer@gmail.com"))
            i.putExtra(Intent.EXTRA_SUBJECT, "TesterFeedback")
            i.putExtra(
                Intent.EXTRA_TEXT,
                "($androidVersion\n$modelVersion)" +
                        "$separator" +
                        "$questionQuitUnexp$answer" +
                        "$questionSound$answer" +
                        "$otherProblems$answer" +
                        "$anySuggestions$answer" +
                        "THANK YOU!!! : D")
            try {
                startActivity(Intent.createChooser(i, "Send email to Phil..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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

    override fun onFragmentFinished(library: Library) {
        val intent = Intent(this, InstrumentActivity::class.java).apply {
            putExtra("libraryID", "${library.libraryID}")
        }
        startActivity(intent)
    }
}
