package com.amitranofinzi.vimata.data.model

//open class User(
//    val nome: String,
//    val cognome: String,
//    val username: String,
//    val email: String,
//    val password: String,
//    val eta: Int
//)

//
//class Trainer(
//    nome: String,
//    cognome: String,
//    username: String,
//    email: String,
//    password: String,
//    eta: Int,
//    val specializzazione: String // Aggiunta di un attributo specifico per l'Allenatore
//) : User(nome, cognome, username, email, password, eta)

data class Scheda(
    val id: Int,
    val nome: String,
    val esercizi: List<Esercizio>
)

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

