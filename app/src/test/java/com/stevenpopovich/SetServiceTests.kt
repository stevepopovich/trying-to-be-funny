package com.stevenpopovich

import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import com.stevenpopovich.trying_to_be_funny.room.AppDatabase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.*

class SetServiceTests {
    private lateinit var setService: SetService

    @MockK
    private lateinit var mockDatabase: AppDatabase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        setService = SetServiceLocalSavingImpl(mockDatabase)
    }

    @Test
    fun testDeleteSet() {
        val idToDelete = UUID.randomUUID()

        setService.deleteSet(idToDelete)

        verify(exactly = 1) { mockDatabase.setDao().deleteById(idToDelete) }
    }
}