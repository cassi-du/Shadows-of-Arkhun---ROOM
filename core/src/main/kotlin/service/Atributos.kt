package model

import java.io.Serializable

data class Atributos(
    val forca: Int,
    val destreza: Int,
    val constituicao: Int,
    val inteligencia: Int,
    val sabedoria: Int,
    val carisma: Int
) : Serializable
