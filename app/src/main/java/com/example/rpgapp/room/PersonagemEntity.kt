package com.example.rpgapp.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

class AtributosEmbeddable(
    var forca: Int = 0,
    var destreza: Int = 0,
    var constituicao: Int = 0,
    var inteligencia: Int = 0,
    var sabedoria: Int = 0,
    var carisma: Int = 0
)

@Entity(tableName = "personagens")
data class PersonagemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val raca: String,
    val classe: String,
    val estilo: String,
    @Embedded val atributos: AtributosEmbeddable
)
