package com.amitranofinzi.vimata.data.model

data class TestSet (
    val id: String = "",
    val title: String = "",
    val trainerID: String = "",
    val athleteID: String = "",
    val testIDs : List<String> = emptyList()
)
{    constructor() : this(
    id = "",
    title = "",
    trainerID = "",
    athleteID = "",
    testIDs = emptyList(),
    )
}