package com.example.rpgapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rpgapp.ui.theme.RpgAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RpgAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaInicial()
                }
            }
        }
    }
}

@Composable
fun TelaInicial() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bem-vindo ao Old Dragon!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            val intent = Intent(context, MainActivity2::class.java)
            context.startActivity(intent)
        }) {
            Text("Criar Personagem")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            val intent = Intent(context, ListCharactersActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Listar Personagens")
        }
    }
}
