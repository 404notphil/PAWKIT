package com.tunepruner.fingerperc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.tunepruner.fingerperc.instrument.Instrument

class LaunchScreenActivity : AppCompatActivity() {
    private lateinit var instrument: Instrument

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)

        findViewById<Button>(R.id.bomboleguero).setOnClickListener { view ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("libraryName", "bomboleguero")
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.cajon).setOnClickListener { view ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("libraryName", "cajon")
            }
            startActivity(intent)
        }
    }
}