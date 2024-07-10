package com.amitranofinzi.vimata.data.model

data class Test(
    val id: String = "",
    val testSetID: String = "",
    val exerciseName: String = "",
    val videoUrl: String = "",
    val result: Double = 0.0,
    val unit: String = "", //unit of measure of result
    val comment: String = "",
    val trainerID: String? = "",
    val athleteID: String? = ""
)
{    constructor() : this(
        id = "",
        testSetID = "",
        exerciseName = "",
        videoUrl = "",
        result = 0.0,
        unit = "",
        comment = "",
        trainerID = null,
        athleteID = null,
    )
}