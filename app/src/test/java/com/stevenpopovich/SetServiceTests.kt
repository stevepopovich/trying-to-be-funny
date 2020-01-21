package com.stevenpopovich

import android.content.Context
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*

class SetServiceTests {
    private lateinit var setService: SetService

    @Before
    fun setUp() {
        setService = SetServiceLocalSavingImpl(mock(Context::class.java))
    }

    @Test
    fun testDeleteSet() {
        setService.deleteSet(UUID.randomUUID())

        verify(setService)
    }
}