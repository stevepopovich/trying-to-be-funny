package com.stevenpopovich

import com.stevenpopovich.trying_to_be_funny.Place
import com.stevenpopovich.trying_to_be_funny.SetDataInvalidError
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import com.stevenpopovich.trying_to_be_funny.room.AppDatabase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
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

        runBlocking {
            setService.saveStaticSetAsync().await()
        }
    }

    @Test(expected = SetDataInvalidError::class)
    fun testSaveStaticSetThrowsWhenSetLocationIsNull() {
        SetService.setLocation = null

        runBlocking {
            setService.saveStaticSetAsync().await()
        }
    }

    @Test(expected = SetDataInvalidError::class)
    fun testSaveStaticSetThrowsWhenSetRecordingIdIsNull() {
        SetService.setRecordingId = null

        runBlocking {
            setService.saveStaticSetAsync().await()
        }
    }

    @Test
    fun testSaveStaticSet() {
        val bits = listOf("Funny how?")
        val location = Place(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        val recordingId = "This is the location of the recording locally"

        SetService.setBits = bits
        SetService.setLocation = location
        SetService.setRecordingId = recordingId

        runBlocking {
            setService.saveStaticSetAsync().await()

            verify(exactly = 1) { mockDatabase.placeDao().insert(
                withArg {
                    assert(it.name == location.name)
                    assert(it.placeId == location.id)
                })
            }

            verify(exactly = 1) { mockDatabase.setDao().insert(
                withArg {
                    assert(it.placeId == location.id)
                    assert(it.recordingId == recordingId)
                    assert(it.date.isPrettyMuchNow())
                })
            }
        }
    }
}