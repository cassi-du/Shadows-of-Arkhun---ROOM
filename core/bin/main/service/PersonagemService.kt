package service

import enums.Classe
import enums.EstiloRolagem
import enums.Raca
import model.Atributos
import model.Personagem
import kotlin.random.Random

object PersonagemService {

    private fun rolarDado(lados: Int): Int = Random.nextInt(1, lados + 1)

    fun rolarAtributos(estilo: EstiloRolagem): List<Int> {
        return when (estilo) {
            EstiloRolagem.CLASSICO, EstiloRolagem.AVENTUREIRO -> {
                List(6) { (1..3).sumOf { rolarDado(6) } }
            }
            EstiloRolagem.HEROICO -> {
                List(6) {
                    val rolagens = List(4) { rolarDado(6) }
                    rolagens.sortedDescending().take(3).sum()
                }
            }
        }
    }

    fun criarPersonagem(
        nome: String,
        raca: Raca,
        classe: Classe,
        estilo: EstiloRolagem,
        atributos: Atributos
    ): Personagem {
        return Personagem(nome, raca, classe, estilo, atributos)
    }
}
