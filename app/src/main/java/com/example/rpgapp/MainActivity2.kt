package com.example.rpgapp

import android.content.Intent
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
import enums.Classe
import enums.EstiloRolagem
import enums.Raca
import com.example.rpgapp.ui.theme.RpgAppTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RpgAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaCriacaoBasica()
                }
            }
        }
    }
}

@Composable
fun TelaCriacaoBasica() {
    val contexto = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var racaSelecionada by remember { mutableStateOf<Raca?>(null) }
    var classeSelecionada by remember { mutableStateOf<Classe?>(null) }
    var estiloSelecionado by remember { mutableStateOf<EstiloRolagem?>(null) }

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {
        Text("Criação de Personagem", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Personagem") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownEnum("Raça", Raca.entries.toList(), racaSelecionada) { racaSelecionada = it }
        Spacer(modifier = Modifier.height(16.dp))

        DropdownEnum("Classe", Classe.entries.toList(), classeSelecionada) { classeSelecionada = it }
        Spacer(modifier = Modifier.height(16.dp))

        DropdownEnum("Estilo de Rolagem", EstiloRolagem.entries.toList(), estiloSelecionado) { estiloSelecionado = it }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nome.isNotBlank() && racaSelecionada != null && classeSelecionada != null && estiloSelecionado != null) {
                    val intent = Intent(contexto, MainActivity3::class.java).apply {
                        putExtra("NOME", nome)
                        putExtra("RACA", racaSelecionada!!.name)
                        putExtra("CLASSE", classeSelecionada!!.name)
                        putExtra("ESTILO", estiloSelecionado!!.name)
                    }
                    contexto.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar")
        }
    }
}

@Composable
fun <T> DropdownEnum(
    label: String,
    valores: List<T>,
    selecionado: T?,
    onSelecionar: (T) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    Column {
        Text(text = label)
        Box {
            Button(onClick = { expandido = true }) {
                Text(selecionado?.toString() ?: "Selecione")
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                valores.forEach { valor ->
                    DropdownMenuItem(
                        text = { Text(valor.toString()) },
                        onClick = {
                            onSelecionar(valor)
                            expandido = false
                        }
                    )
                }
            }
        }
    }
}
