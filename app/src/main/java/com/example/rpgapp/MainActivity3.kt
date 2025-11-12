package com.example.rpgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import controller.service.criarPersonagemUI
import enums.Classe
import enums.EstiloRolagem
import enums.Raca
import model.Atributos
import model.Personagem
import com.example.rpgapp.ui.theme.RpgAppTheme
import service.PersonagemService

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nome = intent.getStringExtra("NOME") ?: ""
        val raca = intent.getStringExtra("RACA")?.let { Raca.valueOf(it) } ?: Raca.HUMANO
        val classe = intent.getStringExtra("CLASSE")?.let { Classe.valueOf(it) } ?: Classe.GUERREIRO
        val estilo = intent.getStringExtra("ESTILO")?.let { EstiloRolagem.valueOf(it) } ?: EstiloRolagem.CLASSICO
        setContent {
            RpgAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaDistribuicaoAtributos(nome, raca, classe, estilo)
                }
            }
        }
    }
}

@Composable
fun TelaDistribuicaoAtributos(
    nome: String,
    raca: Raca,
    classe: Classe,
    estilo: EstiloRolagem
) {
    val scroll = rememberScrollState()
    val contexto = LocalContext.current

    // Atributos rolados inicialmente
    val valoresRolados = remember { PersonagemService.rolarAtributos(estilo) }
    // Estado dos atributos do personagem
    var atributos by remember { mutableStateOf(Atributos(0,0,0,0,0,0)) }
    // Estado para guardar o valor selecionado de cada atributo
    val selecionados = remember { mutableStateListOf<Int?>(null, null, null, null, null, null) }
    // Lista de números disponíveis para seleção
    val numerosDisponiveis = remember { mutableStateListOf<Int>().apply { addAll(valoresRolados) } }

    if (estilo == EstiloRolagem.CLASSICO) {
        // Preenche automaticamente os atributos e vai direto para a próxima tela
        LaunchedEffect(Unit) {
            atributos = Atributos(
                valoresRolados[0], valoresRolados[1], valoresRolados[2],
                valoresRolados[3], valoresRolados[4], valoresRolados[5]
            )
            val personagem: Personagem = criarPersonagemUI(nome, raca, classe, estilo, atributos)
            val intent = android.content.Intent(contexto, MainActivity4::class.java)
            intent.putExtra("NOME", personagem.nome)
            intent.putExtra("RACA", personagem.raca.name)
            intent.putExtra("CLASSE", personagem.classe.name)
            intent.putExtra("ESTILO", personagem.estiloRolagem.name)
            intent.putExtra("FORCA", personagem.atributos.forca)
            intent.putExtra("DESTREZA", personagem.atributos.destreza)
            intent.putExtra("CONSTITUICAO", personagem.atributos.constituicao)
            intent.putExtra("INTELIGENCIA", personagem.atributos.inteligencia)
            intent.putExtra("SABEDORIA", personagem.atributos.sabedoria)
            intent.putExtra("CARISMA", personagem.atributos.carisma)
            contexto.startActivity(intent)
        }
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scroll)
        ) {
            Text("Distribua os atributos", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // Exibe lista dos números rolados disponíveis
            Text("Números disponíveis: " + numerosDisponiveis.joinToString(", "))
            Spacer(modifier = Modifier.height(16.dp))

            val atributosNomes = listOf("Força", "Destreza", "Constituição", "Inteligência", "Sabedoria", "Carisma")
            atributosNomes.forEachIndexed { index, nomeAtributo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(nomeAtributo)
                    var expandido by remember { mutableStateOf(false) }
                    Box {
                        val valorAtual = selecionados[index]
                        Button(onClick = { expandido = true }, enabled = numerosDisponiveis.isNotEmpty() || valorAtual != null) {
                            Text(text = valorAtual?.toString() ?: "Selecione")
                        }
                        DropdownMenu(
                            expanded = expandido,
                            onDismissRequest = { expandido = false }
                        ) {
                            // Mostra apenas os números disponíveis + o já selecionado para esse atributo
                            val opcoes = if (valorAtual != null) listOf(valorAtual) + numerosDisponiveis else numerosDisponiveis
                            opcoes.distinct().forEach { valor ->
                                DropdownMenuItem(
                                    text = { Text(valor.toString()) },
                                    onClick = {
                                        // Se já havia um valor selecionado, devolve para a lista
                                        val anterior = selecionados[index]
                                        if (anterior != null) {
                                            numerosDisponiveis.add(anterior)
                                        }
                                        // Remove o novo valor da lista
                                        numerosDisponiveis.remove(valor)
                                        selecionados[index] = valor
                                        // Atualiza atributos
                                        when(index){
                                            0 -> atributos = atributos.copy(forca = valor)
                                            1 -> atributos = atributos.copy(destreza = valor)
                                            2 -> atributos = atributos.copy(constituicao = valor)
                                            3 -> atributos = atributos.copy(inteligencia = valor)
                                            4 -> atributos = atributos.copy(sabedoria = valor)
                                            5 -> atributos = atributos.copy(carisma = valor)
                                        }
                                        expandido = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Cria personagem com atributos selecionados
                    val personagem: Personagem = criarPersonagemUI(nome, raca, classe, estilo, atributos)
                    val intent = android.content.Intent(contexto, MainActivity4::class.java)
                    intent.putExtra("NOME", personagem.nome)
                    intent.putExtra("RACA", personagem.raca.name)
                    intent.putExtra("CLASSE", personagem.classe.name)
                    intent.putExtra("ESTILO", personagem.estiloRolagem.name)
                    intent.putExtra("FORCA", personagem.atributos.forca)
                    intent.putExtra("DESTREZA", personagem.atributos.destreza)
                    intent.putExtra("CONSTITUICAO", personagem.atributos.constituicao)
                    intent.putExtra("INTELIGENCIA", personagem.atributos.inteligencia)
                    intent.putExtra("SABEDORIA", personagem.atributos.sabedoria)
                    intent.putExtra("CARISMA", personagem.atributos.carisma)
                    contexto.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selecionados.all { it != null }
            ) {
                Text("Finalizar Personagem")
            }
        }
    }
}