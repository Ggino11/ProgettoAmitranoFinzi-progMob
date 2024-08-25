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

    private lateinit var workoutDao: WorkoutDao

    @Before
    fun setup() {
        // Mock the WorkoutDao interface
        workoutDao = mock(WorkoutDao::class.java)
    }

    @Test
    fun getWorkoutById_retrieveWorkoutByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val workout = Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout.pdf")
        `when`(workoutDao.getWorkoutById("workout1")).thenReturn(workout)

        // Act
        val result = workoutDao.getWorkoutById("workout1")

        // Assert
        assertEquals(workout, result)
        verify(workoutDao).getWorkoutById("workout1")
    }

    @Test
    fun getWorkoutById_noWorkoutFound(): Unit = runBlocking {
        // Arrange
        `when`(workoutDao.getWorkoutById("non_existing_workout")).thenReturn(null)

        // Act
        val result = workoutDao.getWorkoutById("non_existing_workout")

        // Assert
        assertNull(result)
        verify(workoutDao).getWorkoutById("non_existing_workout")
    }

    @Test
    fun getWorkoutsByTrainer_retrieveWorkoutsByTrainerID(): Unit = runBlocking {
        // Arrange
        val workouts = listOf(
            Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout1.pdf"),
            Workout(id = "workout2", title = "Evening Routine", status = "Scheduled", trainerID = "trainer1", athleteID = "athlete2", pdfUrl = "http://example.com/workout2.pdf")
        )
        `when`(workoutDao.getWorkoutsByTrainer("trainer1")).thenReturn(workouts)

        // Act
        val result = workoutDao.getWorkoutsByTrainer("trainer1")

        // Assert
        assertEquals(workouts, result)
        verify(workoutDao).getWorkoutsByTrainer("trainer1")
    }

    @Test
    fun getWorkoutsByAthlete_retrieveWorkoutsByAthleteID(): Unit = runBlocking {
        // Arrange
        val workouts = listOf(
            Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout1.pdf"),
            Workout(id = "workout2", title = "Evening Routine", status = "Scheduled", trainerID = "trainer2", athleteID = "athlete1", pdfUrl = "http://example.com/workout2.pdf")
        )
        `when`(workoutDao.getWorkoutsByAthlete("athlete1")).thenReturn(workouts)

        // Act
        val result = workoutDao.getWorkoutsByAthlete("athlete1")

        // Assert
        assertEquals(workouts, result)
        verify(workoutDao).getWorkoutsByAthlete("athlete1")
    }

    @Test
    fun getWorkoutsByAthleteAndTrainer_retrieveWorkoutsByAthleteAndTrainerID(): Unit = runBlocking {
        // Arrange
        val workouts = listOf(
            Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout1.pdf")
        )
        `when`(workoutDao.getWorkoutsByAthleteAndTrainer("athlete1", "trainer1")).thenReturn(workouts)

        // Act
        val result = workoutDao.getWorkoutsByAthleteAndTrainer("athlete1", "trainer1")

        // Assert
        assertEquals(workouts, result)
        verify(workoutDao).getWorkoutsByAthleteAndTrainer("athlete1", "trainer1")
    }

    @Test
    fun getAllWorkouts_retrieveAllWorkouts(): Unit = runBlocking {
        // Arrange
        val workouts = listOf(
            Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout1.pdf"),
            Workout(id = "workout2", title = "Evening Routine", status = "Scheduled", trainerID = "trainer2", athleteID = "athlete2", pdfUrl = "http://example.com/workout2.pdf")
        )
        `when`(workoutDao.getAllWorkouts()).thenReturn(workouts)

        // Act
        val result = workoutDao.getAllWorkouts()

        // Assert
        assertEquals(workouts, result)
        verify(workoutDao).getAllWorkouts()
    }

    @Test
    fun insertWorkoutAndRetrieve(): Unit = runBlocking {
        // Arrange
        val workout = Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout.pdf")

        // Act
        workoutDao.insert(workout)
        `when`(workoutDao.getWorkoutById("workout1")).thenReturn(workout)
        val retrievedWorkout = workoutDao.getWorkoutById("workout1")

        // Assert
        assertEquals(workout, retrievedWorkout)
        verify(workoutDao).insert(workout)
        verify(workoutDao).getWorkoutById("workout1")
    }

    @Test
    fun insertAllWorkoutsAndRetrieve(): Unit = runBlocking {
        // Arrange
        val workouts = listOf(
            Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout1.pdf"),
            Workout(id = "workout2", title = "Evening Routine", status = "Scheduled", trainerID = "trainer2", athleteID = "athlete2", pdfUrl = "http://example.com/workout2.pdf")
        )

        // Act
        workoutDao.insertAll(workouts)
        `when`(workoutDao.getAllWorkouts()).thenReturn(workouts)
        val result = workoutDao.getAllWorkouts()

        // Assert
        assertEquals(workouts, result)
        verify(workoutDao).insertAll(workouts)
        verify(workoutDao).getAllWorkouts()
    }

    @Test
    fun updateWorkout_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val workout = Workout(id = "workout1", title = "Morning Routine", status = "Completed", trainerID = "trainer1", athleteID = "athlete1", pdfUrl = "http://example.com/workout.pdf")
        val updatedWorkout = workout.copy(status = "Updated")
        `when`(workoutDao.getWorkoutById("workout1")).thenReturn(updatedWorkout)

        // Act
        workoutDao.update(updatedWorkout)
        val retrievedWorkout = workoutDao.getWorkoutById("workout1")

        // Assert
        assertEquals(updatedWorkout, retrievedWorkout)
        verify(workoutDao).update(updatedWorkout)
        verify(workoutDao).getWorkoutById("workout1")
    }
}
