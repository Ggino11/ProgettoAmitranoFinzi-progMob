package com.amitranofinzi.vimata.data.dao

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

    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setup() {
        // Mock the ExerciseDao interface
        exerciseDao = mock(ExerciseDao::class.java)
    }

    @Test
    fun getExerciseById_retrieveExerciseById(): Unit = runBlocking {
        // Arrange
        val exercise = Exercise(
            id = "exercise1",
            name = "Push-up",
            description = "A basic upper body exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        `when`(exerciseDao.getExerciseById("exercise1")).thenReturn(exercise)

        // Act
        val result = exerciseDao.getExerciseById("exercise1")

        // Assert
        assertEquals(exercise, result)
        verify(exerciseDao).getExerciseById("exercise1")
    }

    @Test
    fun getExercisesByTrainerID_retrieveExercisesByTrainerId(): Unit = runBlocking {
        // Arrange
        val exercise1 = Exercise(
            id = "exercise1",
            name = "Push-up",
            description = "A basic upper body exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val exercise2 = Exercise(
            id = "exercise2",
            name = "Squat",
            description = "A basic lower body exercise.",
            videoUrl = "http://example.com/squat",
            trainerID = "trainer1",
            collectionID = "collection2"
        )
        `when`(exerciseDao.getExercisesByTrainerID("trainer1")).thenReturn(listOf(exercise1, exercise2))

        // Act
        val result = exerciseDao.getExercisesByTrainerID("trainer1")

        // Assert
        assertEquals(listOf(exercise1, exercise2), result)
        verify(exerciseDao).getExercisesByTrainerID("trainer1")
    }

    @Test
    fun getExercisesByCollectionID_retrieveExercisesByCollectionId(): Unit = runBlocking {
        // Arrange
        val exercise1 = Exercise(
            id = "exercise1",
            name = "Push-up",
            description = "A basic upper body exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val exercise2 = Exercise(
            id = "exercise3",
            name = "Plank",
            description = "A core stability exercise.",
            videoUrl = "http://example.com/plank",
            trainerID = "trainer2",
            collectionID = "collection1"
        )
        `when`(exerciseDao.getExercisesByCollectionID("collection1")).thenReturn(listOf(exercise1, exercise2))

        // Act
        val result = exerciseDao.getExercisesByCollectionID("collection1")

        // Assert
        assertEquals(listOf(exercise1, exercise2), result)
        verify(exerciseDao).getExercisesByCollectionID("collection1")
    }

    @Test
    fun insertAndRetrieveExercise(): Unit = runBlocking {
        // Arrange
        val exercise = Exercise(
            id = "exercise1",
            name = "Push-up",
            description = "A basic upper body exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )

        // Act
        exerciseDao.insert(exercise)
        `when`(exerciseDao.getExerciseById("exercise1")).thenReturn(exercise)
        val retrievedExercise = exerciseDao.getExerciseById("exercise1")

        // Assert
        assertEquals(exercise, retrievedExercise)
        verify(exerciseDao).insert(exercise)
        verify(exerciseDao).getExerciseById("exercise1")
    }

    @Test
    fun insertAllAndRetrieveExercises(): Unit = runBlocking {
        // Arrange
        val exercise1 = Exercise(
            id = "exercise1",
            name = "Push-up",
            description = "A basic upper body exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val exercise2 = Exercise(
            id = "exercise2",
            name = "Squat",
            description = "A basic lower body exercise.",
            videoUrl = "http://example.com/squat",
            trainerID = "trainer2",
            collectionID = "collection2"
        )
        val exercises = listOf(exercise1, exercise2)

        // Act
        exerciseDao.insertAll(exercises)
        `when`(exerciseDao.getExercisesByTrainerID("trainer1")).thenReturn(listOf(exercise1))
        val result = exerciseDao.getExercisesByTrainerID("trainer1")

        // Assert
        assertEquals(listOf(exercise1), result)
        verify(exerciseDao).insertAll(exercises)
        verify(exerciseDao).getExercisesByTrainerID("trainer1")
    }

    @Test
    fun updateExercise_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val exercise = Exercise(
            id = "exercise1",
            name = "Push-up",
            description = "A basic upper body exercise.",
            videoUrl = "http://example.com/pushup",
            trainerID = "trainer1",
            collectionID = "collection1"
        )
        val updatedExercise = exercise.copy(description = "An updated description for Push-up.")
        `when`(exerciseDao.getExerciseById("exercise1")).thenReturn(updatedExercise)

        // Act
        exerciseDao.update(updatedExercise)
        val retrievedExercise = exerciseDao.getExerciseById("exercise1")

        // Assert
        assertEquals(updatedExercise, retrievedExercise)
        verify(exerciseDao).update(updatedExercise)
        verify(exerciseDao).getExerciseById("exercise1")
    }

    @Test
    fun getExerciseById_noExerciseFound(): Unit = runBlocking {
        // Arrange
        `when`(exerciseDao.getExerciseById("non_existing_exercise_id")).thenReturn(null)

        // Act
        val retrievedExercise = exerciseDao.getExerciseById("non_existing_exercise_id")

        // Assert
        assertNull(retrievedExercise)
        verify(exerciseDao).getExerciseById("non_existing_exercise_id")
    }
}
