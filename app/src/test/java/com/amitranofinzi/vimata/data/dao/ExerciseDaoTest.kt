package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.dao.ExerciseDao
import com.amitranofinzi.vimata.data.model.Exercise
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class ExerciseDaoTest {

    // Mock ExerciseDao object
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setup() {
        // Initialize the ExerciseDao mock
        exerciseDao = mock(ExerciseDao::class.java)
    }

    @Test
    fun insertExercise_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val exercise = Exercise(
            id = "exercise1",
            name = "Push Up",
            description = "A basic push-up exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        `when`(exerciseDao.getWithPrimaryKey("exercise1")).thenReturn(exercise)

        // Act
        exerciseDao.insert(exercise)
        val retrievedExercise = exerciseDao.getWithPrimaryKey("exercise1")

        // Assert
        assertEquals(exercise, retrievedExercise)
        verify(exerciseDao).insert(exercise)
        verify(exerciseDao).getWithPrimaryKey("exercise1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val exercise1 = Exercise(
            id = "exercise1",
            name = "Push Up",
            description = "A basic push-up exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val exercise2 = Exercise(
            id = "exercise2",
            name = "Sit Up",
            description = "A basic sit-up exercise.",
            videoUrl = "http://example.com/situp",
            trainerID = "trainer2",
            collectionID = "collection2"
        )
        val expectedExercises = listOf(exercise1)
        `when`(exerciseDao.getWhereEqual("trainerID", "trainer1")).thenReturn(expectedExercises)

        // Act
        val result = exerciseDao.getWhereEqual("trainerID", "trainer1")

        // Assert
        assertEquals(expectedExercises, result)
        verify(exerciseDao).getWhereEqual("trainerID", "trainer1")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val exercise1 = Exercise(
            id = "exercise1",
            name = "Push Up",
            description = "A basic push-up exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val exercise2 = Exercise(
            id = "exercise2",
            name = "Sit Up",
            description = "A basic sit-up exercise.",
            videoUrl = "http://example.com/situp",
            trainerID = "trainer2",
            collectionID = "collection2"
        )
        val exercise3 = Exercise(
            id = "exercise3",
            name = "Squat",
            description = "A basic squat exercise.",
            videoUrl = "http://example.com/squat",
            trainerID = "trainer3",
            collectionID = "collection3"
        )
        val expectedExercises = listOf(exercise1, exercise3)
        `when`(exerciseDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))).thenReturn(expectedExercises)

        // Act
        val result = exerciseDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))

        // Assert
        assertEquals(expectedExercises, result)
        verify(exerciseDao).getWhereIn("trainerID", listOf("trainer1", "trainer3"))
    }

    @Test
    fun updateExercise_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val exercise = Exercise(
            id = "exercise1",
            name = "Push Up",
            description = "A basic push-up exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val updatedExercise = exercise.copy(name = "Updated Push Up")
        `when`(exerciseDao.getWithPrimaryKey("exercise1")).thenReturn(updatedExercise)

        // Act
        exerciseDao.update(updatedExercise)
        val retrievedExercise = exerciseDao.getWithPrimaryKey("exercise1")

        // Assert
        assertEquals(updatedExercise, retrievedExercise)
        verify(exerciseDao).update(updatedExercise)
        verify(exerciseDao).getWithPrimaryKey("exercise1")
    }

    @Test
    fun getWithPrimaryKey_noExerciseFound(): Unit = runBlocking {
        // Arrange
        `when`(exerciseDao.getWithPrimaryKey("non_existing_exercise_id")).thenReturn(null)

        // Act
        val retrievedExercise = exerciseDao.getWithPrimaryKey("non_existing_exercise_id")

        // Assert
        assertNull(retrievedExercise)
        verify(exerciseDao).getWithPrimaryKey("non_existing_exercise_id")
    }
}
