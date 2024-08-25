package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.model.Collection
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CollectionDaoTest {

    private lateinit var collectionDao: CollectionDao

    @Before
    fun setup() {
        // Mock the CollectionDao interface
        collectionDao = mock(CollectionDao::class.java)
    }

    @Test
    fun getById_retrieveCollectionById(): Unit = runBlocking {
        // Arrange
        val collection = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")
        `when`(collectionDao.getById("collection1")).thenReturn(listOf(collection))

        // Act
        val result = collectionDao.getById("collection1")

        // Assert
        assertEquals(listOf(collection), result)
        verify(collectionDao).getById("collection1")
    }

    @Test
    fun getAll_retrieveAllCollections(): Unit = runBlocking {
        // Arrange
        val collection1 = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")
        val collection2 = Collection(id = "collection2", title = "Workout Plan B", trainerID = "trainer2")
        `when`(collectionDao.getAll()).thenReturn(listOf(collection1, collection2))

        // Act
        val result = collectionDao.getAll()

        // Assert
        assertEquals(listOf(collection1, collection2), result)
        verify(collectionDao).getAll()
    }

    @Test
    fun getCollectionById_retrieveSingleCollectionById(): Unit = runBlocking {
        // Arrange
        val collection = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")
        `when`(collectionDao.getCollectionById("collection1")).thenReturn(collection)

        // Act
        val result = collectionDao.getCollectionById("collection1")

        // Assert
        assertEquals(collection, result)
        verify(collectionDao).getCollectionById("collection1")
    }

    @Test
    fun getCollectionsByTrainerId_retrieveCollectionsByTrainerId(): Unit = runBlocking {
        // Arrange
        val collection1 = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")
        val collection2 = Collection(id = "collection2", title = "Workout Plan B", trainerID = "trainer1")
        `when`(collectionDao.getCollectionsByTrainerId("trainer1")).thenReturn(listOf(collection1, collection2))

        // Act
        val result = collectionDao.getCollectionsByTrainerId("trainer1")

        // Assert
        assertEquals(listOf(collection1, collection2), result)
        verify(collectionDao).getCollectionsByTrainerId("trainer1")
    }

    @Test
    fun insertAndRetrieveCollection(): Unit = runBlocking {
        // Arrange
        val collection = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")

        // Act
        collectionDao.insert(collection)
        `when`(collectionDao.getCollectionById("collection1")).thenReturn(collection)
        val retrievedCollection = collectionDao.getCollectionById("collection1")

        // Assert
        assertEquals(collection, retrievedCollection)
        verify(collectionDao).insert(collection)
        verify(collectionDao).getCollectionById("collection1")
    }

    @Test
    fun insertAllAndRetrieveCollections(): Unit = runBlocking {
        // Arrange
        val collection1 = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")
        val collection2 = Collection(id = "collection2", title = "Workout Plan B", trainerID = "trainer2")
        val collections = listOf(collection1, collection2)

        // Act
        collectionDao.insertAll(collections)
        `when`(collectionDao.getAll()).thenReturn(collections)
        val result = collectionDao.getAll()

        // Assert
        assertEquals(collections, result)
        verify(collectionDao).insertAll(collections)
        verify(collectionDao).getAll()
    }

    @Test
    fun updateCollection_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val collection = Collection(id = "collection1", title = "Workout Plan A", trainerID = "trainer1")
        val updatedCollection = collection.copy(title = "Updated Workout Plan A")
        `when`(collectionDao.getCollectionById("collection1")).thenReturn(updatedCollection)

        // Act
        collectionDao.update(updatedCollection)
        val retrievedCollection = collectionDao.getCollectionById("collection1")

        // Assert
        assertEquals(updatedCollection, retrievedCollection)
        verify(collectionDao).update(updatedCollection)
        verify(collectionDao).getCollectionById("collection1")
    }

    @Test
    fun getById_noCollectionFound(): Unit = runBlocking {
        // Arrange
        `when`(collectionDao.getCollectionById("non_existing_collection_id")).thenReturn(null)

        // Act
        val retrievedCollection = collectionDao.getCollectionById("non_existing_collection_id")

        // Assert
        assertNull(retrievedCollection)
        verify(collectionDao).getCollectionById("non_existing_collection_id")
    }
}
