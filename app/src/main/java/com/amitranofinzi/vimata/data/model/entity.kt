package com.amitranofinzi.vimata.data.model

data class Workout(
    val id: String,
    val title: String,
    val status: String,
    val trainerID: String?,
    val athleteID: String?,
    val pdfUrl: String
)
{    constructor() : this(
    id = "",
    title = "",
    status = "",
    trainerID = null,
    athleteID = null,
    pdfUrl = ""
)

}
data class Esercizio(
    val id: Int,
    val nome: String,
    val descrizione: String,
    val ripetizioni: Int,
    val serie: Int
)

data class Test(
    val id: Int,
    val nome: String,
    val descrizione: String,
    val data: String
)

