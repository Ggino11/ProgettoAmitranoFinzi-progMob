package com.amitranofinzi.vimata.data.model

data class Relationship(
    val id: String,
    val athleteID: String,
    val trainerID: String? //{TODO: makle changes for ? operator }
) {
    constructor() : this (
        id = "",
        athleteID = "",
        trainerID = ""
    )
}