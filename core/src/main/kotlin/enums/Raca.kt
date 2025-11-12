package enums

enum class Raca(
    val movimento: Int,
    val infravisao: Int?,
    val alinhamento: String,
    val habilidades: List<String>
) {
    HUMANO(
        movimento = 9,
        infravisao = null,
        alinhamento = "Qualquer",
        habilidades = listOf("Aprendizado", "Adaptabilidade")
    ),
    ANAO(
        movimento = 6,
        infravisao = 18,
        alinhamento = "Ordem",
        habilidades = listOf("Mineradores", "Vigoroso", "Armas Grandes", "Inimigos")
    ),
    ELFO(
        movimento = 9,
        infravisao = 18,
        alinhamento = "Neutro",
        habilidades = listOf("Percepção Natural", "Graciosos", "Arma Racial", "Imunidades")
    )
}