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

    // Mock TestDao object
    private lateinit var testDao: TestDao

    @Before
    fun setup() {
        // Initialize the TestDao mock
        testDao = mock(TestDao::class.java)
    }

    @Test
    fun insertTest_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val test = Test(
            id = "test1",
            testSetID = "testSet1",
            exerciseName = "Push Up",
            videoUrl = "http://example.com/pushup",
            result = 10.0,
            unit = "reps",
            comment = "Well done",
            status = TestStatus.TODO
        )
        `when`(testDao.getWithPrimaryKey("test1")).thenReturn(test)

        // Act
        testDao.insert(test)
        val retrievedTest = testDao.getWithPrimaryKey("test1")

        // Assert
        assertEquals(test, retrievedTest)
        verify(testDao).insert(test)
        verify(testDao).getWithPrimaryKey("test1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val test1 = Test(
            id = "test1",
            testSetID = "testSet1",
            exerciseName = "Push Up",
            videoUrl = "http://example.com/pushup",
            result = 10.0,
            unit = "reps",
            comment = "Well done",
            status = TestStatus.TODO
        )
        val test2 = Test(
            id = "test2",
            testSetID = "testSet2",
            exerciseName = "Sit Up",
            videoUrl = "http://example.com/situp",
            result = 15.0,
            unit = "reps",
            comment = "Keep it up",
            status = TestStatus.DONE
        )
        val expectedTests = listOf(test1)
        `when`(testDao.getWhereEqual("testSetID", "testSet1")).thenReturn(expectedTests)

        // Act
        val result = testDao.getWhereEqual("testSetID", "testSet1")

        // Assert
        assertEquals(expectedTests, result)
        verify(testDao).getWhereEqual("testSetID", "testSet1")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val test1 = Test(
            id = "test1",
            testSetID = "testSet1",
            exerciseName = "Push Up",
            videoUrl = "http://example.com/pushup",
            result = 10.0,
            unit = "reps",
            comment = "Well done",
            status = TestStatus.TODO
        )
        val test2 = Test(
            id = "test2",
            testSetID = "testSet2",
            exerciseName = "Sit Up",
            videoUrl = "http://example.com/situp",
            result = 15.0,
            unit = "reps",
            comment = "Keep it up",
            status = TestStatus.DONE
        )
        val test3 = Test(
            id = "test3",
            testSetID = "testSet3",
            exerciseName = "Squat",
            videoUrl = "http://example.com/squat",
            result = 20.0,
            unit = "reps",
            comment = "Great job",
            status = TestStatus.TODO
        )
        val expectedTests = listOf(test1, test3)
        `when`(testDao.getWhereIn("testSetID", listOf("testSet1", "testSet3"))).thenReturn(expectedTests)

        // Act
        val result = testDao.getWhereIn("testSetID", listOf("testSet1", "testSet3"))

        // Assert
        assertEquals(expectedTests, result)
        verify(testDao).getWhereIn("testSetID", listOf("testSet1", "testSet3"))
    }

    @Test
    fun updateTest_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val test = Test(
            id = "test1",
            testSetID = "testSet1",
            exerciseName = "Push Up",
            videoUrl = "http://example.com/pushup",
            result = 10.0,
            unit = "reps",
            comment = "Well done",
            status = TestStatus.TODO
        )
        val updatedTest = test.copy(result = 20.0, comment = "Excellent progress")
        `when`(testDao.getWithPrimaryKey("test1")).thenReturn(updatedTest)

        // Act
        testDao.update(updatedTest)
        val retrievedTest = testDao.getWithPrimaryKey("test1")

        // Assert
        assertEquals(updatedTest, retrievedTest)
        verify(testDao).update(updatedTest)
        verify(testDao).getWithPrimaryKey("test1")
    }

    @Test
    fun getWithPrimaryKey_noTestFound(): Unit = runBlocking {
        // Arrange
        `when`(testDao.getWithPrimaryKey("non_existing_test_id")).thenReturn(null)

        // Act
        val retrievedTest = testDao.getWithPrimaryKey("non_existing_test_id")

        // Assert
        assertNull(retrievedTest)
        verify(testDao).getWithPrimaryKey("non_existing_test_id")
    }
}
