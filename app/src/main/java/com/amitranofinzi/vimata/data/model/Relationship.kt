package com.amitranofinzi.vimata.data.model

data class Relationship(
    val id: String,
    val athleteID: String,
    val trainerID: String?
) {
    constructor() : this (
        id = "",
        athleteID = "",
        trainerID = ""
    )
}