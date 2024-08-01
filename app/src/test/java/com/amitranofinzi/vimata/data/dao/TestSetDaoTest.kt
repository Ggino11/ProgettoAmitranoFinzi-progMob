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

    // Mock TestSetDao object
    private lateinit var testSetDao: TestSetDao

    @Before
    fun setup() {
        // Initialize the TestSetDao mock
        testSetDao = mock(TestSetDao::class.java)
    }

    @Test
    fun insertTestSet_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val testSet = TestSet(
            id = "testSet1",
            title = "Strength Training",
            trainerID = "trainer1",
            athleteID = "athlete1"
        )
        `when`(testSetDao.getWithPrimaryKey("testSet1")).thenReturn(testSet)

        // Act
        testSetDao.insert(testSet)
        val retrievedTestSet = testSetDao.getWithPrimaryKey("testSet1")

        // Assert
        assertEquals(testSet, retrievedTestSet)
        verify(testSetDao).insert(testSet)
        verify(testSetDao).getWithPrimaryKey("testSet1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val testSet1 = TestSet(
            id = "testSet1",
            title = "Strength Training",
            trainerID = "trainer1",
            athleteID = "athlete1"
        )
        val testSet2 = TestSet(
            id = "testSet2",
            title = "Cardio Training",
            trainerID = "trainer1",
            athleteID = "athlete2"
        )
        val expectedTestSets = listOf(testSet1)
        `when`(testSetDao.getWhereEqual("trainerID", "trainer1")).thenReturn(expectedTestSets)

        // Act
        val result = testSetDao.getWhereEqual("trainerID", "trainer1")

        // Assert
        assertEquals(expectedTestSets, result)
        verify(testSetDao).getWhereEqual("trainerID", "trainer1")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val testSet1 = TestSet(
            id = "testSet1",
            title = "Strength Training",
            trainerID = "trainer1",
            athleteID = "athlete1"
        )
        val testSet2 = TestSet(
            id = "testSet2",
            title = "Cardio Training",
            trainerID = "trainer2",
            athleteID = "athlete2"
        )
        val testSet3 = TestSet(
            id = "testSet3",
            title = "Flexibility Training",
            trainerID = "trainer3",
            athleteID = "athlete3"
        )
        val expectedTestSets = listOf(testSet1, testSet3)
        `when`(testSetDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))).thenReturn(expectedTestSets)

        // Act
        val result = testSetDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))

        // Assert
        assertEquals(expectedTestSets, result)
        verify(testSetDao).getWhereIn("trainerID", listOf("trainer1", "trainer3"))
    }

    @Test
    fun updateTestSet_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val testSet = TestSet(
            id = "testSet1",
            title = "Strength Training",
            trainerID = "trainer1",
            athleteID = "athlete1"
        )
        val updatedTestSet = testSet.copy(title = "Advanced Strength Training")
        `when`(testSetDao.getWithPrimaryKey("testSet1")).thenReturn(updatedTestSet)

        // Act
        testSetDao.update(updatedTestSet)
        val retrievedTestSet = testSetDao.getWithPrimaryKey("testSet1")

        // Assert
        assertEquals(updatedTestSet, retrievedTestSet)
        verify(testSetDao).update(updatedTestSet)
        verify(testSetDao).getWithPrimaryKey("testSet1")
    }

    @Test
    fun getWithPrimaryKey_noTestSetFound(): Unit = runBlocking {
        // Arrange
        `when`(testSetDao.getWithPrimaryKey("non_existing_testSet_id")).thenReturn(null)

        // Act
        val retrievedTestSet = testSetDao.getWithPrimaryKey("non_existing_testSet_id")

        // Assert
        assertNull(retrievedTestSet)
        verify(testSetDao).getWithPrimaryKey("non_existing_testSet_id")
    }
}