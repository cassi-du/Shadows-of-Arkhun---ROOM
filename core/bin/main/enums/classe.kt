package enums

enum class Classe(
    val armas: List<String>,
    val armaduras: List<String>,
    val itensMagicos: List<String>,
    val habilidadesClasse: List<String>
) {
    GUERREIRO(
        armas = listOf("Todas"),
        armaduras = listOf("Todas"),
        itensMagicos = listOf("Sem cajados/varinhas", "Pode pergaminhos de proteção"),
        habilidadesClasse = listOf("Treinamento Militar", "Liderança em batalha")
    ),
    MAGO(
        armas = listOf("Pequenas (adagas, bordão)"),
        armaduras = listOf("Nenhuma"),
        itensMagicos = listOf("Pode usar cajados, varinhas e pergaminhos"),
        habilidadesClasse = listOf("Conjuração de Magias", "Conhecimento Arcano")
    ),
    NECROMANTE(
        armas = listOf("Pequenas"),
        armaduras = listOf("Nenhuma"),
        itensMagicos = listOf("Magias exclusivas: Toque Sombrio, Aterrorizar"),
        habilidadesClasse = listOf("Magias Necróticas", "Criar Mortos-Vivos", "Drenar Vida")
    )
}
