package com.stevenpopovich

import android.Manifest
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.stevenpopovich.trying_to_be_funny.ui.main.RecorderFragment
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class RecorderFragmentTests {
    private lateinit var recorderFragment: RecorderFragment;

    @Before
    fun setUp() {
        recorderFragment = RecorderFragment()
    }

    @Test
    fun testThatThePauseButtonIsShownAboveVersionN() {
        val scenario = launchFragmentInContainer<RecorderFragment>(null)
    }

    @Test
    fun testThat() {

        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        verify(recorderFragment).requestPermissions(
            listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
            0
        )
    }
}

//askForNotGrantedPermissions(context!!, activity!!,
//listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE))