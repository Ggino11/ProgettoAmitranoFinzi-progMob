package com.amitranofinzi.vimata.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitranofinzi.vimata.data.dao.CollectionDao
import com.amitranofinzi.vimata.data.dao.ExerciseDao
import com.amitranofinzi.vimata.data.dao.RelationshipDao
import com.amitranofinzi.vimata.data.dao.TestDao
import com.amitranofinzi.vimata.data.dao.TestSetDao
import com.amitranofinzi.vimata.data.dao.UserDao
import com.amitranofinzi.vimata.data.dao.WorkoutDao
import com.amitranofinzi.vimata.data.database.AppDatabase
import com.amitranofinzi.vimata.data.model.Collection
import com.amitranofinzi.vimata.data.model.Exercise
import com.amitranofinzi.vimata.data.model.Test
import com.amitranofinzi.vimata.data.model.TestSet
import com.amitranofinzi.vimata.data.model.User
import com.amitranofinzi.vimata.data.model.Workout
import com.amitranofinzi.vimata.data.repository.TestRepository
import com.amitranofinzi.vimata.data.repository.TrainerRepository
import com.amitranofinzi.vimata.data.repository.WorkbookRepository
import com.amitranofinzi.vimata.ui.navigation.InitializableViewModel
import kotlinx.coroutines.launch


/**
 * ViewModel for managing trainer-related data, including athletes, workouts, tests, collections, and exercises.
 * It interacts with various repositories to fetch and manipulate data.
 */
class TrainerViewModel: ViewModel(), InitializableViewModel {
    lateinit var appDatabase: AppDatabase
    lateinit var context: Context
    private var isInitialized = false
    private lateinit var trainerRepository: TrainerRepository
    private lateinit var workbookRepository: WorkbookRepository
    private lateinit var testRepository: TestRepository

    private val relationshipDao: RelationshipDao
        get() = appDatabase.relationshipDao()
    private val userDao: UserDao
        get() = appDatabase.userDao()
    private val workoutDao: WorkoutDao
        get() = appDatabase.workoutDao()

    private val collectionDao: CollectionDao
        get() = appDatabase.collectionDao()
    private val exerciseDao: ExerciseDao
        get() = appDatabase.exerciseDao()

    private val testDao: TestDao
        get() = appDatabase.testDao()
    private val testSetDao: TestSetDao
        get() = appDatabase.testSetDao()

    /**
     * Initializes the ViewModel with the provided database and context.
     * This should be called before using the ViewModel.
     *
     * @param appDatabase The application database instance.
     * @param context The application context.
     */
    override fun initialize(appDatabase: AppDatabase, context: Context) {
        if (!isInitialized) {
            this.appDatabase = appDatabase
            this.context = context

            // Initialize repositories after appDatabase and context are set
            initializeRepositories()

            isInitialized = true
        }
    }

    private fun initializeRepositories() {
        trainerRepository = TrainerRepository(relationshipDao, userDao, workoutDao, context)
        workbookRepository = WorkbookRepository(collectionDao, exerciseDao, workoutDao, context)
        testRepository = TestRepository(testDao, testSetDao, context)
    }

    private val _athletes = MutableLiveData<List<User>>()
    val athletes: LiveData<List<User>> = _athletes

    // Collection Live Data
    private val _collections = MutableLiveData<List<Collection>>()
    val collections: LiveData<List<Collection>> = _collections

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _collection = MutableLiveData<Collection>()
    val collection: LiveData<Collection> = _collection

    private val _testSets = MutableLiveData<List<TestSet>>()
    val testSets: LiveData<List<TestSet>>  get() = _testSets

    private val _tests = MutableLiveData<List<Test>>()
    val tests: LiveData<List<Test>> get() = _tests

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>> = _workouts

    private val _selectedExercises = MutableLiveData<List<Exercise>>()
    val selectedExercises: LiveData<List<Exercise>> = _selectedExercises

    /**
     * Fetches athletes assigned to a specific coach.
     *
     * @param coachId The ID of the coach.
     */
    fun getAthletesForCoach(coachId: String) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Launching coroutine")
                val athleteIds = trainerRepository.getAthleteIdsForCoach(coachId)
                Log.d("TrainerViewModel", "Athlete IDs: $athleteIds")
                val athleteDetails = trainerRepository.getAthletes(athleteIds)
                Log.d("TrainerViewModel", "Athlete details fetched: $athleteDetails")
                _athletes.value = athleteDetails
                Log.d("TrainerViewModel", "Athlete details assigned to LiveData")

            } catch (e: Exception) {
                // Handle the error
                Log.d("trainerViewModel","error" )
                _athletes.value = emptyList()
            }
        }
    }

    /**
     * Fetches workouts for a specific athlete managed by a trainer.
     *
     * @param athleteID The ID of the athlete.
     * @param trainerID The ID of the trainer.
     */
    fun fetchWorkouts(athleteID: String, trainerID: String){
        viewModelScope.launch {
            _workouts.value = trainerRepository.getAthleteWorkoutsByTrainer(athleteID,trainerID)
        }
    }

    /**
     * Fetches test sets for a specific athlete.
     *
     * @param athleteID The ID of the athlete.
     */
    fun fetchTestSets(athleteID: String) {
        Log.d("testViewModel","fetchTestSets" )

        viewModelScope.launch {
            try {
                val fetchedTestSets = testRepository.getTestSetsForAthlete(athleteID)
                Log.d("testViewModel", "Fetched ${fetchedTestSets.size} test sets")
                fetchedTestSets.forEach {
                    Log.d("testViewModel", "TestSet: ${it.title}")
                }
                _testSets.setValue(fetchedTestSets)
                Log.d("testViewModel", "Test sets updated in LiveData")
            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error fetching testSets" )
                _testSets.setValue(emptyList())
            }
        }
    }

    /**
     * Creates a new test set and associated tests based on the selected exercises.
     *
     * @param testSetTitle The title of the test set.
     * @param trainerID The ID of the trainer.
     * @param athleteID The ID of the athlete.
     * @param selectedExercises The list of exercises to include in the test set.
     */
    fun createTestSetAndTests(testSetTitle: String, trainerID: String, athleteID: String, selectedExercises: List<Exercise>) {
        viewModelScope.launch {
            try {

                val newTestSet = TestSet(
                    title = testSetTitle,
                    trainerID = trainerID,
                    athleteID = athleteID
                )
                Log.d("TrainerViewModel", "new TestSet request ${newTestSet.toString()}")

                val testSetId = testRepository.createTestSet(newTestSet)

                val tests = selectedExercises.map { exercise ->
                    Test(
                        testSetID = testSetId,
                        exerciseName = exercise.name,
                    )
                }

                tests.forEach { test ->
                    testRepository.createTest(test)
                }

                Log.d("TrainerViewModel", "TestSet and Tests created successfully")
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error creating TestSet and Tests", e)
                // Gestione dell'errore, se necessario
            }
        }
    }

    /**
     * Fetches tests belonging to a specific test set.
     *
     * @param testSetId The ID of the test set.
     */
    fun fetchTests(testSetId: String?) {
        viewModelScope.launch {
            try {
                val fetchedTests = testRepository.getTests(testSetId)
                Log.d("testViewModel", "Fetched ${fetchedTests.size} test sets")
                fetchedTests.forEach {
                    Log.d("testViewModel", "TestSet: ${it.exerciseName}")
                }
                _tests.setValue(fetchedTests)
            } catch (e: Exception) {
                // Handle the error
                Log.d("testViewModel","error fetching tests" )

            }
        }
    }


    /**
     * Fetches collections for a specific trainer.
     *
     * @param trainerID The ID of the trainer.
     */
    fun fetchCollections(trainerID: String) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Fetching collections for trainerID: $trainerID")
                val fetchedCollections = workbookRepository.getCollections(trainerID)
                Log.d("TrainerViewModel", "Fetched collections: $fetchedCollections")
                _collections.value = fetchedCollections
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error fetching collections", e)
                _collections.value = emptyList()
            }
        }
    }

    /**
     * Fetches a specific collection by its ID.
     *
     * @param collectionID The ID of the collection.
     */
    fun fetchCollection(collectionID: String){
        viewModelScope.launch {
            val fetchedCollection = workbookRepository.getCollectionByID(collectionID)
            _collection.value = fetchedCollection
        }
    }

    /**
     * Fetches exercises for a specific collection.
     *
     * @param collectionID The ID of the collection.
     */
    fun fetchExercises(collectionID: String?) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Fetching exercises for exerciseIdList: $collectionID")
                val fetchedExercises = workbookRepository.getExercises(collectionID)
                Log.d("TrainerViewModel", "Fetched exercises: $fetchedExercises")
                _exercises.value = fetchedExercises
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error fetching exercises", e)
                _exercises.value = emptyList()
            }
        }
    }

    /**
     * Adds a new collection to the repository and refreshes the list of collections.
     *
     * @param collection The collection to add.
     * @param trainerID The ID of the trainer.
     */
    fun addCollection(collection: Collection, trainerID: String) {
        viewModelScope.launch {
            workbookRepository.addCollection(collection.copy(trainerID = trainerID))
            fetchCollections(trainerID) // Refresh the list
        }
    }

    /**
     * Adds a new exercise to the repository.
     *
     * @param exercise The exercise to add.
     */
    fun addExerciseToCollection(exercise: Exercise) {
        viewModelScope.launch {
            workbookRepository.uploadExercise( exercise.copy())
        }
    }

    /**
     * Fetches exercises for a specific trainer.
     *
     * @param trainerID The ID of the trainer.
     */
    fun fetchExercisesByTrainerId(trainerID: String) {
        viewModelScope.launch {
            try {
                Log.d("TrainerViewModel", "Fetching exercises for trainerID: $trainerID")
                val fetchedExercises = workbookRepository.getExercisesByTrainerId(trainerID)
                Log.d("TrainerViewModel", "Fetched ${fetchedExercises.size} exercises")
                _exercises.value = fetchedExercises
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error fetching exercises", e)
                _exercises.value = emptyList()
            }
        }
    }

    /**
     * Adds an exercise to the list of selected exercises.
     *
     * @param exercise The exercise to add.
     */
    fun addSelectedExercise(exercise: Exercise) {
        val currentList = _selectedExercises.value ?: emptyList()
        if (!currentList.contains(exercise)) {
            _selectedExercises.value = currentList + exercise
        }
    }

    /**
     * Removes an exercise from the list of selected exercises.
     *
     * @param exercise The exercise to remove.
     */
    fun removeSelectedExercise(exercise: Exercise) {
        val currentList = _selectedExercises.value ?: emptyList()
        _selectedExercises.value = currentList - exercise
    }

    /**
     * Sets the list of selected exercises.
     *
     * @param exercises The list of exercises to set.
     */
    fun setSelectedExercises(exercises: List<Exercise>) {
        _selectedExercises.value = exercises
    }

    /**
     * Adds a new workout to the repository.
     *
     * @param workout The workout to add.
     */
    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            workbookRepository.uploadWorkout( workout.copy())
        }
    }


}


