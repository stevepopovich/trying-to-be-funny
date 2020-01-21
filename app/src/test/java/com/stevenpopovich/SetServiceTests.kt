package com.stevenpopovich

import com.stevenpopovich.trying_to_be_funny.SetDataInvalidError
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

    @Test
    fun testGetAllBits() {
        setService.getAllBits()

        verify(exactly = 1) { mockDatabase.bitDao().getAll() }
    }

    @Test
    fun testGetSet() {
        val idToGet = UUID.randomUUID()

        setService.getSet(idToGet)

        verify(exactly = 1) { mockDatabase.setDao().get(idToGet) }
    }

    @Test(expected = SetDataInvalidError::class)
    fun testSaveStaticSetThrowsWhenSetBitsIsNull() {
        SetService.setBits = null

        setService.saveStaticSet()
    }
}