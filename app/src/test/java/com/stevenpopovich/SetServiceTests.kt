package com.stevenpopovich

import android.content.Context
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import org.junit.Before
import org.mockito.Mockito.mock

class SetServiceTests {
    private lateinit var setService: SetService

    @Before
    fun setUp() {
        setService = SetServiceLocalSavingImpl(mock(Context::class.java))
    }
}