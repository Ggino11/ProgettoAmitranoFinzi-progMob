package com.amitranofinzi.vimata.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.amitranofinzi.vimata.data.extensions.TestStatus
import com.google.firebase.firestore.DocumentId
/**
 * Represents a test within a test set.
 * This is a Room entity annotated for database table creation.
 * The `tests` table has a foreign key relationship with the `test_sets` table.
 *
 * @property id The unique ID of the test. And the firebase document ID
 * @property testSetID The ID of the test set to which this test belongs.
 * @property exerciseName The name of the exercise being tested.
 * @property videoUrl The URL of the video for the test.
 * @property result The result of the test.
 * @property unit The unit of measure for the result.
 * @property comment Any comments associated with the test.
 * @property status The status of the test.
 */
@Entity(tableName = "tests",
    foreignKeys = [
        ForeignKey(
            entity = TestSet::class,
            parentColumns = ["id"],
            childColumns = ["testSetID"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [androidx.room.Index(value = ["testSetID"])]
)
data class Test(
    @PrimaryKey @DocumentId @NonNull val id: String = "",
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



