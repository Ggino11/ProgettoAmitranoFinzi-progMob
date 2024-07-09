package com.amitranofinzi.vimata.data.model

data class Relationship (
    val athleteID: String,
    val trainerID: String
) {
    constructor() : this (
        athleteID = "",
        trainerID = ""
    )
}