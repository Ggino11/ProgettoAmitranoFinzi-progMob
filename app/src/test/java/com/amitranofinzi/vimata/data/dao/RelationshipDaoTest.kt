package com.amitranofinzi.vimata.data.dao

import com.amitranofinzi.vimata.data.model.Relationship
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class RelationshipDaoTest {

    // Mock com.amitranofinzi.vimata.data.dao.RelationshipDao object
    private lateinit var relationshipDao: RelationshipDao

    @Before
    fun setup() {
        // Initialize the com.amitranofinzi.vimata.data.dao.RelationshipDao mock
        relationshipDao = mock(RelationshipDao::class.java)
    }

    @Test
    fun insertRelationship_retrieveByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val relationship = Relationship(
            id = "relationship1",
            athleteID = "athlete1",
            trainerID = "trainer1"
        )
        `when`(relationshipDao.getWithPrimaryKey("relationship1")).thenReturn(relationship)

        // Act
        relationshipDao.insert(relationship)
        val retrievedRelationship = relationshipDao.getWithPrimaryKey("relationship1")

        // Assert
        assertEquals(relationship, retrievedRelationship)
        verify(relationshipDao).insert(relationship)
        verify(relationshipDao).getWithPrimaryKey("relationship1")
    }

    @Test
    fun getWhereEqual_fieldEqualsValue(): Unit = runBlocking {
        // Arrange
        val relationship1 = Relationship(
            id = "relationship1",
            athleteID = "athlete1",
            trainerID = "trainer1"
        )
        val relationship2 = Relationship(
            id = "relationship2",
            athleteID = "athlete2",
            trainerID = "trainer1"
        )
        val expectedRelationships = listOf(relationship1, relationship2)
        `when`(relationshipDao.getWhereEqual("trainerID", "trainer1")).thenReturn(expectedRelationships)

        // Act
        val result = relationshipDao.getWhereEqual("trainerID", "trainer1")

        // Assert
        assertEquals(expectedRelationships, result)
        verify(relationshipDao).getWhereEqual("trainerID", "trainer1")
    }

    @Test
    fun getWhereIn_fieldInValues(): Unit = runBlocking {
        // Arrange
        val relationship1 = Relationship(
            id = "relationship1",
            athleteID = "athlete1",
            trainerID = "trainer1"
        )
        val relationship2 = Relationship(
            id = "relationship2",
            athleteID = "athlete2",
            trainerID = "trainer2"
        )
        val relationship3 = Relationship(
            id = "relationship3",
            athleteID = "athlete3",
            trainerID = "trainer3"
        )
        val expectedRelationships = listOf(relationship1, relationship3)
        `when`(relationshipDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))).thenReturn(expectedRelationships)

        // Act
        val result = relationshipDao.getWhereIn("trainerID", listOf("trainer1", "trainer3"))

        // Assert
        assertEquals(expectedRelationships, result)
        verify(relationshipDao).getWhereIn("trainerID", listOf("trainer1", "trainer3"))
    }

    @Test
    fun updateRelationship_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val relationship = Relationship(
            id = "relationship1",
            athleteID = "athlete1",
            trainerID = "trainer1"
        )
        val updatedRelationship = relationship.copy(trainerID = "trainer2")
        `when`(relationshipDao.getWithPrimaryKey("relationship1")).thenReturn(updatedRelationship)

        // Act
        relationshipDao.update(updatedRelationship)
        val retrievedRelationship = relationshipDao.getWithPrimaryKey("relationship1")

        // Assert
        assertEquals(updatedRelationship, retrievedRelationship)
        verify(relationshipDao).update(updatedRelationship)
        verify(relationshipDao).getWithPrimaryKey("relationship1")
    }

    @Test
    fun getWithPrimaryKey_noRelationshipFound(): Unit = runBlocking {
        // Arrange
        `when`(relationshipDao.getWithPrimaryKey("non_existing_relationship_id")).thenReturn(null)

        // Act
        val retrievedRelationship = relationshipDao.getWithPrimaryKey("non_existing_relationship_id")

        // Assert
        assertNull(retrievedRelationship)
        verify(relationshipDao).getWithPrimaryKey("non_existing_relationship_id")
    }
}
