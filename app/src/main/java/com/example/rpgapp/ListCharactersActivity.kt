package com.example.rpgapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Personagem

class ListCharactersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CharactersScreen()
            }
        }
    }
}

@Composable
fun CharactersScreen() {
    val list = remember { mutableStateListOf<Personagem>() }
    val contexto = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val db = com.example.rpgapp.room.AppDatabase.getInstance(contexto)
            // executar em IO
            val items = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                db.personagemDao().getAll()
            }
            // map entities to model.Personagem
            val mapped = items.map { e ->
                val raca = try { enums.Raca.valueOf(e.raca) } catch (ex: Exception) { enums.Raca.HUMANO }
                val classe = try { enums.Classe.valueOf(e.classe) } catch (ex: Exception) { enums.Classe.GUERREIRO }
                val estilo = try { enums.EstiloRolagem.valueOf(e.estilo) } catch (ex: Exception) { enums.EstiloRolagem.CLASSICO }
                val atributos = model.Atributos(
                    e.atributos.forca,
                    e.atributos.destreza,
                    e.atributos.constituicao,
                    e.atributos.inteligencia,
                    e.atributos.sabedoria,
                    e.atributos.carisma
                )
                model.Personagem(e.nome, raca, classe, estilo, atributos)
            }
            list.clear()
            list.addAll(mapped)
        } catch (e: Exception) {
            Log.e("ListCharactersActivity", "Erro ao carregar personagens: ${e.message}", e)
            list.clear()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Botão para voltar à tela inicial
        Button(onClick = { (contexto as Activity).finish() }, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Personagens Salvos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        if (list.isEmpty()) {
            Text("Nenhum personagem salvo ainda.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(list) { p ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp), colors = CardDefaults.cardColors()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = p.nome, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Raça: ${p.raca.name} | Classe: ${p.classe.name}")
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Força: ${p.atributos.forca} | Destreza: ${p.atributos.destreza}")
                            Text(text = "Constituição: ${p.atributos.constituicao} | Inteligência: ${p.atributos.inteligencia}")
                            Text(text = "Sabedoria: ${p.atributos.sabedoria} | Carisma: ${p.atributos.carisma}")
                        }
                    }
                }
            }
        }
    }
}