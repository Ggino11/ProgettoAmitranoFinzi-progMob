package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.model.TestSet
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TestSetDaoTest {

    private lateinit var testSetDao: TestSetDao

    @Before
    fun setup() {
        // Mock the TestSetDao interface
        testSetDao = mock(TestSetDao::class.java)
    }

    @Test
    fun getTestSetById_retrieveTestSetByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val testSet = TestSet(id = "testSet1", title = "Test Set 1", trainerID = "trainer1", athleteID = "athlete1")
        `when`(testSetDao.getTestSetById("testSet1")).thenReturn(testSet)

        // Act
        val result = testSetDao.getTestSetById("testSet1")

        // Assert
        assertEquals(testSet, result)
        verify(testSetDao).getTestSetById("testSet1")
    }

    @Test
    fun getTestSetsByAthleteId_retrieveTestSetsByAthleteId(): Unit = runBlocking {
        // Arrange
        val testSets = listOf(
            TestSet(id = "testSet1", title = "Test Set 1", trainerID = "trainer1", athleteID = "athlete1"),
            TestSet(id = "testSet2", title = "Test Set 2", trainerID = "trainer2", athleteID = "athlete1")
        )
        `when`(testSetDao.getTestSetsByAthleteId("athlete1")).thenReturn(testSets)

        // Act
        val result = testSetDao.getTestSetsByAthleteId("athlete1")

        // Assert
        assertEquals(testSets, result)
        verify(testSetDao).getTestSetsByAthleteId("athlete1")
    }

    @Test
    fun insertAndRetrieveTestSet(): Unit = runBlocking {
        // Arrange
        val testSet = TestSet(id = "testSet1", title = "Test Set 1", trainerID = "trainer1", athleteID = "athlete1")

        // Act
        testSetDao.insert(testSet)
        `when`(testSetDao.getTestSetById("testSet1")).thenReturn(testSet)
        val retrievedTestSet = testSetDao.getTestSetById("testSet1")

        // Assert
        assertEquals(testSet, retrievedTestSet)
        verify(testSetDao).insert(testSet)
        verify(testSetDao).getTestSetById("testSet1")
    }

    @Test
    fun insertAllAndRetrieveTestSets(): Unit = runBlocking {
        // Arrange
        val testSets = listOf(
            TestSet(id = "testSet1", title = "Test Set 1", trainerID = "trainer1", athleteID = "athlete1"),
            TestSet(id = "testSet2", title = "Test Set 2", trainerID = "trainer2", athleteID = "athlete1")
        )

        // Act
        testSetDao.insertAll(testSets)
        `when`(testSetDao.getTestSetsByAthleteId("athlete1")).thenReturn(testSets)
        val result = testSetDao.getTestSetsByAthleteId("athlete1")

        // Assert
        assertEquals(testSets, result)
        verify(testSetDao).insertAll(testSets)
        verify(testSetDao).getTestSetsByAthleteId("athlete1")
    }

    @Test
    fun updateTestSet_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val testSet = TestSet(id = "testSet1", title = "Test Set 1", trainerID = "trainer1", athleteID = "athlete1")
        val updatedTestSet = testSet.copy(title = "Updated Test Set")
        `when`(testSetDao.getTestSetById("testSet1")).thenReturn(updatedTestSet)

        // Act
        testSetDao.update(updatedTestSet)
        val retrievedTestSet = testSetDao.getTestSetById("testSet1")

        // Assert
        assertEquals(updatedTestSet, retrievedTestSet)
        verify(testSetDao).update(updatedTestSet)
        verify(testSetDao).getTestSetById("testSet1")
    }

    @Test
    fun getTestSetById_noTestSetFound(): Unit = runBlocking {
        // Arrange
        `when`(testSetDao.getTestSetById("non_existing_id")).thenReturn(null)

        // Act
        val retrievedTestSet = testSetDao.getTestSetById("non_existing_id")

        // Assert
        assertNull(retrievedTestSet)
        verify(testSetDao).getTestSetById("non_existing_id")
    }
}
