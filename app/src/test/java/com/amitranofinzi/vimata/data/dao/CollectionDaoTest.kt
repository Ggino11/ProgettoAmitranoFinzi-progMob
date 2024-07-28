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

    // Mock CollectionDao object
    private lateinit var collectionDao: CollectionDao

    @Before
    fun setup() {
        // Initialize the CollectionDao mock
        collectionDao = mock(CollectionDao::class.java)
    }

    @Test
    fun insertCollection_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val collection = Collection(
            id = "collection1",
            title = "Collection Title",
            trainerID = "trainer1"
        )
        `when`(collectionDao.getWithPrimaryKey("collection1")).thenReturn(collection)

        // Act
        collectionDao.insert(collection)
        val retrievedCollection = collectionDao.getWithPrimaryKey("collection1")

        // Assert
        assertEquals(collection, retrievedCollection)
        verify(collectionDao).insert(collection)
        verify(collectionDao).getWithPrimaryKey("collection1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val collection1 = Collection(
            id = "collection1",
            title = "Collection Title",
            trainerID = "trainer1"
        )
        val collection2 = Collection(
            id = "collection2",
            title = "Another Collection Title",
            trainerID = "trainer2"
        )
        val expectedCollections = listOf(collection1)
        `when`(collectionDao.getWhereEqual("trainerID", "trainer1")).thenReturn(expectedCollections)

        // Act
        val result = collectionDao.getWhereEqual("trainerID", "trainer1")

        // Assert
        assertEquals(expectedCollections, result)
        verify(collectionDao).getWhereEqual("trainerID", "trainer1")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val collection1 = Collection(
            id = "collection1",
            title = "Collection Title",
            trainerID = "trainer1"
        )
        val collection2 = Collection(
            id = "collection2",
            title = "Another Collection Title",
            trainerID = "trainer2"
        )
        val collection3 = Collection(
            id = "collection3",
            title = "Third Collection Title",
            trainerID = "trainer3"
        )
        val expectedCollections = listOf(collection1, collection3)
        `when`(collectionDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))).thenReturn(expectedCollections)

        // Act
        val result = collectionDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))

        // Assert
        assertEquals(expectedCollections, result)
        verify(collectionDao).getWhereIn("trainerID", listOf("trainer1", "trainer3"))
    }

    @Test
    fun updateCollection_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val collection = Collection(
            id = "collection1",
            title = "Collection Title",
            trainerID = "trainer1"
        )
        val updatedCollection = collection.copy(title = "Updated Collection Title")
        `when`(collectionDao.getWithPrimaryKey("collection1")).thenReturn(updatedCollection)

        // Act
        collectionDao.update(updatedCollection)
        val retrievedCollection = collectionDao.getWithPrimaryKey("collection1")

        // Assert
        assertEquals(updatedCollection, retrievedCollection)
        verify(collectionDao).update(updatedCollection)
        verify(collectionDao).getWithPrimaryKey("collection1")
    }

    @Test
    fun getWithPrimaryKey_noCollectionFound(): Unit = runBlocking {
        // Arrange
        `when`(collectionDao.getWithPrimaryKey("non_existing_collection_id")).thenReturn(null)

        // Act
        val retrievedCollection = collectionDao.getWithPrimaryKey("non_existing_collection_id")

        // Assert
        assertNull(retrievedCollection)
        verify(collectionDao).getWithPrimaryKey("non_existing_collection_id")
    }
}
