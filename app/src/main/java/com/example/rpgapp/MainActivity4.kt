package com.example.rpgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Personagem
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.rpgapp.room.AppDatabase
import com.example.rpgapp.room.PersonagemEntity
import com.example.rpgapp.room.AtributosEmbeddable
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recebe os dados via extras
        val nome = intent.getStringExtra("NOME") ?: ""
        val raca = intent.getStringExtra("RACA")?.let { enums.Raca.valueOf(it) } ?: enums.Raca.HUMANO
        val classe = intent.getStringExtra("CLASSE")?.let { enums.Classe.valueOf(it) } ?: enums.Classe.GUERREIRO
        val estilo = intent.getStringExtra("ESTILO")?.let { enums.EstiloRolagem.valueOf(it) } ?: enums.EstiloRolagem.CLASSICO
        val atributos = model.Atributos(
            intent.getIntExtra("FORCA", 0),
            intent.getIntExtra("DESTREZA", 0),
            intent.getIntExtra("CONSTITUICAO", 0),
            intent.getIntExtra("INTELIGENCIA", 0),
            intent.getIntExtra("SABEDORIA", 0),
            intent.getIntExtra("CARISMA", 0)
        )
        val personagem = model.Personagem(nome, raca, classe, estilo, atributos)

        setContent {
            androidx.compose.material3.MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FichaPersonagem(personagem)
                }
            }
        }
    }
}

@Composable
fun FichaPersonagem(p: Personagem) {
    val scroll = rememberScrollState()
    val contexto = LocalContext.current
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {
        Text("Ficha do Personagem", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Nome: ${p.nome}", style = MaterialTheme.typography.titleMedium)
                Text("Raça: ${p.raca.name}", style = MaterialTheme.typography.bodyMedium)
                Text("Movimento: ${p.raca.movimento}", style = MaterialTheme.typography.bodyMedium)
                Text("Infravisão: ${p.raca.infravisao ?: "Nenhuma"}", style = MaterialTheme.typography.bodyMedium)
                Text("Alinhamento: ${p.raca.alinhamento}", style = MaterialTheme.typography.bodyMedium)
                Text("Habilidades da Raça: ${p.raca.habilidades.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Classe: ${p.classe.name}", style = MaterialTheme.typography.titleMedium)
                Text("Armas: ${p.classe.armas.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                Text("Armaduras: ${p.classe.armaduras.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                Text("Itens Mágicos: ${p.classe.itensMagicos.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                Text("Habilidades da Classe: ${p.classe.habilidadesClasse.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                Text("Estilo de Rolagem: ${p.estiloRolagem.name}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Atributos", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Força:", style = MaterialTheme.typography.bodyMedium)
                    Text(p.atributos.forca.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Destreza:", style = MaterialTheme.typography.bodyMedium)
                    Text(p.atributos.destreza.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Constituição:", style = MaterialTheme.typography.bodyMedium)
                    Text(p.atributos.constituicao.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Inteligência:", style = MaterialTheme.typography.bodyMedium)
                    Text(p.atributos.inteligencia.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Sabedoria:", style = MaterialTheme.typography.bodyMedium)
                    Text(p.atributos.sabedoria.toString(), style = MaterialTheme.typography.bodyMedium)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Carisma:", style = MaterialTheme.typography.bodyMedium)
                    Text(p.atributos.carisma.toString(), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Botões para salvar e ver personagens salvos
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                // salvar usando Room em coroutine (DB em IO)
                scope.launch {
                    try {
                        val db = AppDatabase.getInstance(contexto)
                        val exists = withContext(Dispatchers.IO) {
                            db.personagemDao().countByName(p.nome) > 0
                        }
                        if (exists) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(contexto, "Personagem já existe no banco", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val entity = PersonagemEntity(
                                nome = p.nome,
                                raca = p.raca.name,
                                classe = p.classe.name,
                                estilo = p.estiloRolagem.name,
                                atributos = AtributosEmbeddable(
                                    forca = p.atributos.forca,
                                    destreza = p.atributos.destreza,
                                    constituicao = p.atributos.constituicao,
                                    inteligencia = p.atributos.inteligencia,
                                    sabedoria = p.atributos.sabedoria,
                                    carisma = p.atributos.carisma
                                )
                            )
                            withContext(Dispatchers.IO) {
                                db.personagemDao().insert(entity)
                            }
                            withContext(Dispatchers.Main) {
                                Toast.makeText(contexto, "Personagem salvo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity4", "Erro ao salvar personagem: ${e.message}", e)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(contexto, "Erro ao salvar personagem", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar Personagem")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = {
                val intent = android.content.Intent(contexto, ListCharactersActivity::class.java)
                contexto.startActivity(intent)
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Personagens Salvos")
            }
        }
    }
}
