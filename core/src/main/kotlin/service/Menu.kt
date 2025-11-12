package controller

import enums.Classe
import enums.EstiloRolagem
import enums.Raca
object Menu {

    fun listarRacas(): List<Raca> = Raca.values().toList()
    fun listarClasses(): List<Classe> = Classe.values().toList()
    fun listarEstilos(): List<EstiloRolagem> = EstiloRolagem.values().toList()

}
