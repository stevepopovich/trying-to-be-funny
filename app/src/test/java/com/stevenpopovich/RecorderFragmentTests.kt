package com.stevenpopovich

import androidx.fragment.app.testing.launchFragmentInContainer
import com.stevenpopovich.trying_to_be_funny.ui.main.RecorderFragment
import org.junit.Before
import org.junit.Test

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

    }
}