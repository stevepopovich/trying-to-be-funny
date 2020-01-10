package com.stevenpopovich.trying_to_be_funny

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.stevenpopovich.trying_to_be_funny.ui.main.RecorderFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RecorderFragment())
                .commitNow()
        }

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_places_api_key), Locale.US)
        }
    }
}
