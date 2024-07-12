package com.amitranofinzi.vimata.data.model

import com.amitranofinzi.vimata.data.extensions.TestStatus

data class Test(
    val id: String = "",
    val testSetID: String = "",
    val exerciseName: String = "",
    val videoUrl: String = "",
    val result: Double = 0.0,
    val unit: String = "", //unit of measure of result
    val comment: String = "",
    val status: TestStatus = TestStatus.TODO
) {
    constructor() : this(
        id = "",
        testSetID = "",
        exerciseName = "",
        videoUrl = "",
        result = 0.0,
        unit = "",
        comment = "",
        status = TestStatus.TODO
    )



}



