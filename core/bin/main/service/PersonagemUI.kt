package controller.service

import enums.Classe
import enums.EstiloRolagem
import enums.Raca
import model.Atributos
import model.Personagem

fun criarPersonagemUI(
    nome: String,
    raca: Raca,
    classe: Classe,
    estilo: EstiloRolagem,
    atributos: Atributos
): Personagem {
    return Personagem(nome, raca, classe, estilo, atributos)
}
