package com.amitranofinzi.vimata.data.extensions

//for tests status
enum class TestStatus {
    TODO,
    DONE,
    VERIFIED
}

fun TestStatus.toStringValue(): String = this.name

fun String.toTestStatus(): TestStatus = TestStatus.valueOf(this)

