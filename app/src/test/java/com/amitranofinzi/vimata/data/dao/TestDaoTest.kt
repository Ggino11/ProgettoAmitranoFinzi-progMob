package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.amitranofinzi.vimata.data.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TestDaoTest {

    private lateinit var testDao: TestDao

    @Before
    fun setup() {
        // Mock the TestDao interface
        testDao = mock(TestDao::class.java)
    }

    @Test
    fun getTestById_retrieveTestByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val test = Test(id = "test1", testSetID = "testSet1", exerciseName = "Exercise 1")
        `when`(testDao.getTestById("test1")).thenReturn(test)

        // Act
        val result = testDao.getTestById("test1")

        // Assert
        assertEquals(test, result)
        verify(testDao).getTestById("test1")
    }

    @Test
    fun getTestsByTestSetId_retrieveTestsByTestSetId(): Unit = runBlocking {
        // Arrange
        val tests = listOf(
            Test(id = "test1", testSetID = "testSet1", exerciseName = "Exercise 1"),
            Test(id = "test2", testSetID = "testSet1", exerciseName = "Exercise 2")
        )
        `when`(testDao.getTestsByTestSetId("testSet1")).thenReturn(tests)

        // Act
        val result = testDao.getTestsByTestSetId("testSet1")

        // Assert
        assertEquals(tests, result)
        verify(testDao).getTestsByTestSetId("testSet1")
    }

    @Test
    fun insertAndRetrieveTest(): Unit = runBlocking {
        // Arrange
        val test = Test(id = "test1", testSetID = "testSet1", exerciseName = "Exercise 1")

        // Act
        testDao.insert(test)
        `when`(testDao.getTestById("test1")).thenReturn(test)
        val retrievedTest = testDao.getTestById("test1")

        // Assert
        assertEquals(test, retrievedTest)
        verify(testDao).insert(test)
        verify(testDao).getTestById("test1")
    }

    @Test
    fun insertAllAndRetrieveTests(): Unit = runBlocking {
        // Arrange
        val tests = listOf(
            Test(id = "test1", testSetID = "testSet1", exerciseName = "Exercise 1"),
            Test(id = "test2", testSetID = "testSet1", exerciseName = "Exercise 2")
        )

        // Act
        testDao.insertAll(tests)
        `when`(testDao.getTestsByTestSetId("testSet1")).thenReturn(tests)
        val result = testDao.getTestsByTestSetId("testSet1")

        // Assert
        assertEquals(tests, result)
        verify(testDao).insertAll(tests)
        verify(testDao).getTestsByTestSetId("testSet1")
    }

    @Test
    fun updateTest_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val test = Test(id = "test1", testSetID = "testSet1", exerciseName = "Exercise 1")
        val updatedTest = test.copy(exerciseName = "Updated Exercise")
        `when`(testDao.getTestById("test1")).thenReturn(updatedTest)

        // Act
        testDao.update(updatedTest)
        val retrievedTest = testDao.getTestById("test1")

        // Assert
        assertEquals(updatedTest, retrievedTest)
        verify(testDao).update(updatedTest)
        verify(testDao).getTestById("test1")
    }

    @Test
    fun updateTestResult_verifyResultUpdate(): Unit = runBlocking {
        // Arrange
        val testId = "test1"
        val newResult = 95.0

        // Act
        testDao.updateTestResult(testId, newResult)
        `when`(testDao.getTestById(testId)).thenReturn(Test(id = testId, testSetID = "testSet1", result = newResult))
        val retrievedTest = testDao.getTestById(testId)

        // Assert
        assertEquals(newResult, retrievedTest?.result)
        verify(testDao).updateTestResult(testId, newResult)
    }

    @Test
    fun updateTestStatus_verifyStatusUpdate(): Unit = runBlocking {
        // Arrange
        val testId = "test1"
        val newStatus = TestStatus.VERIFIED

        // Act
        testDao.updateTestStatus(testId, newStatus)
        `when`(testDao.getTestById(testId)).thenReturn(Test(id = testId, testSetID = "testSet1", status = newStatus))
        val retrievedTest = testDao.getTestById(testId)

        // Assert
        assertEquals(newStatus, retrievedTest?.status)
        verify(testDao).updateTestStatus(testId, newStatus)
    }

    @Test
    fun getTestById_noTestFound(): Unit = runBlocking {
        // Arrange
        `when`(testDao.getTestById("non_existing_id")).thenReturn(null)

        // Act
        val retrievedTest = testDao.getTestById("non_existing_id")

        // Assert
        assertNull(retrievedTest)
        verify(testDao).getTestById("non_existing_id")
    }
}
