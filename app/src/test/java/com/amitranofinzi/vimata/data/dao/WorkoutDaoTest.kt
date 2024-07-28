package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.model.Workout
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class WorkoutDaoTest {

    // Mock WorkoutDao object
    private lateinit var workoutDao: WorkoutDao

    @Before
    fun setup() {
        // Initialize the WorkoutDao mock
        workoutDao = mock(WorkoutDao::class.java)
    }

    @Test
    fun insertWorkout_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val workout = Workout(
            id = "workout1",
            title = "Workout Title",
            status = "Pending",
            trainerID = "trainer1",
            athleteID = "athlete1",
            pdfUrl = "http://example.com/workout1.pdf"
        )
        `when`(workoutDao.getWithPrimaryKey("workout1")).thenReturn(workout)

        // Act
        workoutDao.insert(workout)
        val retrievedWorkout = workoutDao.getWithPrimaryKey("workout1")

        // Assert
        assertEquals(workout, retrievedWorkout)
        verify(workoutDao).insert(workout)
        verify(workoutDao).getWithPrimaryKey("workout1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val workout1 = Workout(
            id = "workout1",
            title = "Workout Title",
            status = "Pending",
            trainerID = "trainer1",
            athleteID = "athlete1",
            pdfUrl = "http://example.com/workout1.pdf"
        )
        val workout2 = Workout(
            id = "workout2",
            title = "Another Workout Title",
            status = "Completed",
            trainerID = "trainer2",
            athleteID = "athlete2",
            pdfUrl = "http://example.com/workout2.pdf"
        )
        val expectedWorkouts = listOf(workout1)
        `when`(workoutDao.getWhereEqual("status", "Pending")).thenReturn(expectedWorkouts)

        // Act
        val result = workoutDao.getWhereEqual("status", "Pending")

        // Assert
        assertEquals(expectedWorkouts, result)
        verify(workoutDao).getWhereEqual("status", "Pending")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val workout1 = Workout(
            id = "workout1",
            title = "Workout Title",
            status = "Pending",
            trainerID = "trainer1",
            athleteID = "athlete1",
            pdfUrl = "http://example.com/workout1.pdf"
        )
        val workout2 = Workout(
            id = "workout2",
            title = "Another Workout Title",
            status = "Completed",
            trainerID = "trainer2",
            athleteID = "athlete2",
            pdfUrl = "http://example.com/workout2.pdf"
        )
        val workout3 = Workout(
            id = "workout3",
            title = "Third Workout Title",
            status = "Pending",
            trainerID = "trainer3",
            athleteID = "athlete3",
            pdfUrl = "http://example.com/workout3.pdf"
        )
        val expectedWorkouts = listOf(workout1, workout3)
        `when`(workoutDao.getWhereIn("status", listOf("Pending"))).thenReturn(expectedWorkouts)

        // Act
        val result = workoutDao.getWhereIn("status", listOf("Pending"))

        // Assert
        assertEquals(expectedWorkouts, result)
        verify(workoutDao).getWhereIn("status", listOf("Pending"))
    }

    @Test
    fun updateWorkout_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val workout = Workout(
            id = "workout1",
            title = "Workout Title",
            status = "Pending",
            trainerID = "trainer1",
            athleteID = "athlete1",
            pdfUrl = "http://example.com/workout1.pdf"
        )
        val updatedWorkout = workout.copy(status = "Completed")
        `when`(workoutDao.getWithPrimaryKey("workout1")).thenReturn(updatedWorkout)

        // Act
        workoutDao.update(updatedWorkout)
        val retrievedWorkout = workoutDao.getWithPrimaryKey("workout1")

        // Assert
        assertEquals(updatedWorkout, retrievedWorkout)
        verify(workoutDao).update(updatedWorkout)
        verify(workoutDao).getWithPrimaryKey("workout1")
    }

    @Test
    fun getWithPrimaryKey_noWorkoutFound(): Unit = runBlocking {
        // Arrange
        `when`(workoutDao.getWithPrimaryKey("non_existing_workout_id")).thenReturn(null)

        // Act
        val retrievedWorkout = workoutDao.getWithPrimaryKey("non_existing_workout_id")

        // Assert
        assertNull(retrievedWorkout)
        verify(workoutDao).getWithPrimaryKey("non_existing_workout_id")
    }
}
