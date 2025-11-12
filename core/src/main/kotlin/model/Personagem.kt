package model

import enums.Classe
import enums.EstiloRolagem
import enums.Raca
import java.io.Serializable

data class Personagem(
    val nome: String,
    val raca: Raca,
    val classe: Classe,
    val estiloRolagem: EstiloRolagem,
    val atributos: Atributos
) : Serializable {
    override fun toString(): String {
        return """
            Personagem:
              Nome = $nome
              Raça = ${raca.name}
              Classe = ${classe.name}
              Estilo = ${estiloRolagem.name}
              Atributos = $atributos
              Habilidades da Raça = ${raca.habilidades.joinToString(", ")}
              Habilidades da Classe = ${classe.habilidadesClasse.joinToString(", ")}
              Armas = ${classe.armas.joinToString(", ")}
              Armaduras = ${classe.armaduras.joinToString(", ")}
              Itens Mágicos = ${classe.itensMagicos.joinToString(", ")}
        """.trimIndent()
    }
}
