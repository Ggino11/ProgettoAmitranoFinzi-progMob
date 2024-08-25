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

    private lateinit var relationshipDao: RelationshipDao

    @Before
    fun setup() {
        // Mock the RelationshipDao interface
        relationshipDao = mock(RelationshipDao::class.java)
    }

    @Test
    fun getWhereEqual_retrieveRelationshipsByFieldEquality(): Unit = runBlocking {
        // Arrange
        val relationships = listOf(
            Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1"),
            Relationship(id = "rel2", athleteID = "athlete2", trainerID = "trainer1")
        )
        `when`(relationshipDao.getWhereEqual("trainerID", "trainer1")).thenReturn(relationships)

        // Act
        val result = relationshipDao.getWhereEqual("trainerID", "trainer1")

        // Assert
        assertEquals(relationships, result)
        verify(relationshipDao).getWhereEqual("trainerID", "trainer1")
    }

    @Test
    fun getWhereIn_retrieveRelationshipsByFieldInclusion(): Unit = runBlocking {
        // Arrange
        val relationships = listOf(
            Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1"),
            Relationship(id = "rel2", athleteID = "athlete2", trainerID = "trainer1")
        )
        val trainerIDs = listOf("trainer1", "trainer2")
        `when`(relationshipDao.getWhereIn("trainerID", trainerIDs)).thenReturn(relationships)

        // Act
        val result = relationshipDao.getWhereIn("trainerID", trainerIDs)

        // Assert
        assertEquals(relationships, result)
        verify(relationshipDao).getWhereIn("trainerID", trainerIDs)
    }

    @Test
    fun getWhereAthleteID_retrieveRelationshipsByAthleteID(): Unit = runBlocking {
        // Arrange
        val relationships = listOf(
            Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1")
        )
        `when`(relationshipDao.getWhereAthleteID("athlete1")).thenReturn(relationships)

        // Act
        val result = relationshipDao.getWhereAthleteID("athlete1")

        // Assert
        assertEquals(relationships, result)
        verify(relationshipDao).getWhereAthleteID("athlete1")
    }

    @Test
    fun getWithPrimaryKey_retrieveRelationshipByPrimaryKey(): Unit = runBlocking {
        // Arrange
        val relationship = Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1")
        `when`(relationshipDao.getWithPrimaryKey("rel1")).thenReturn(relationship)

        // Act
        val result = relationshipDao.getWithPrimaryKey("rel1")

        // Assert
        assertEquals(relationship, result)
        verify(relationshipDao).getWithPrimaryKey("rel1")
    }

    @Test
    fun getRelationshipsByUserId_retrieveRelationshipsByUserIdAndUserType(): Unit = runBlocking {
        // Arrange
        val relationships = listOf(
            Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1")
        )
        `when`(relationshipDao.getRelationshipsByUserId("athlete1", "athlete")).thenReturn(relationships)

        // Act
        val result = relationshipDao.getRelationshipsByUserId("athlete1", "athlete")

        // Assert
        assertEquals(relationships, result)
        verify(relationshipDao).getRelationshipsByUserId("athlete1", "athlete")
    }

    @Test
    fun getRelationshipsByTrainerId_retrieveRelationshipsByTrainerId(): Unit = runBlocking {
        // Arrange
        val relationships = listOf(
            Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1"),
            Relationship(id = "rel2", athleteID = "athlete2", trainerID = "trainer1")
        )
        `when`(relationshipDao.getRelationshipsByTrainerId("trainer1")).thenReturn(relationships)

        // Act
        val result = relationshipDao.getRelationshipsByTrainerId("trainer1")

        // Assert
        assertEquals(relationships, result)
        verify(relationshipDao).getRelationshipsByTrainerId("trainer1")
    }

    @Test
    fun insertAndRetrieveRelationship(): Unit = runBlocking {
        // Arrange
        val relationship = Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1")

        // Act
        relationshipDao.insert(relationship)
        `when`(relationshipDao.getWithPrimaryKey("rel1")).thenReturn(relationship)
        val retrievedRelationship = relationshipDao.getWithPrimaryKey("rel1")

        // Assert
        assertEquals(relationship, retrievedRelationship)
        verify(relationshipDao).insert(relationship)
        verify(relationshipDao).getWithPrimaryKey("rel1")
    }

    @Test
    fun insertAllAndRetrieveRelationships(): Unit = runBlocking {
        // Arrange
        val relationships = listOf(
            Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1"),
            Relationship(id = "rel2", athleteID = "athlete2", trainerID = "trainer2")
        )

        // Act
        relationshipDao.insertAll(relationships)
        `when`(relationshipDao.getRelationshipsByTrainerId("trainer1")).thenReturn(listOf(relationships[0]))
        val result = relationshipDao.getRelationshipsByTrainerId("trainer1")

        // Assert
        assertEquals(listOf(relationships[0]), result)
        verify(relationshipDao).insertAll(relationships)
        verify(relationshipDao).getRelationshipsByTrainerId("trainer1")
    }

    @Test
    fun updateRelationship_checkUpdatedValues(): Unit = runBlocking {
        // Arrange
        val relationship = Relationship(id = "rel1", athleteID = "athlete1", trainerID = "trainer1")
        val updatedRelationship = relationship.copy(trainerID = "trainer2")
        `when`(relationshipDao.getWithPrimaryKey("rel1")).thenReturn(updatedRelationship)

        // Act
        relationshipDao.update(updatedRelationship)
        val retrievedRelationship = relationshipDao.getWithPrimaryKey("rel1")

        // Assert
        assertEquals(updatedRelationship, retrievedRelationship)
        verify(relationshipDao).update(updatedRelationship)
        verify(relationshipDao).getWithPrimaryKey("rel1")
    }

    @Test
    fun getRelationshipById_noRelationshipFound(): Unit = runBlocking {
        // Arrange
        `when`(relationshipDao.getRelationshipById("non_existing_id")).thenReturn(null)

        // Act
        val retrievedRelationship = relationshipDao.getRelationshipById("non_existing_id")

        // Assert
        assertNull(retrievedRelationship)
        verify(relationshipDao).getRelationshipById("non_existing_id")
    }
}
