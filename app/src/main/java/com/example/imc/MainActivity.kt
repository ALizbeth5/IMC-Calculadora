package com.example.imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "ingreso") {
        composable("ingreso") {
            PantallaIngreso(navController = navController)
        }
        composable(
            route = "resultado/{nombre}/{imc}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("imc") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            // Placeholder para la pantalla de resultados
            val nombre = backStackEntry.arguments?.getString("nombre")
            val imc = backStackEntry.arguments?.getFloat("imc")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Pantalla de Resultado: $nombre tiene un IMC de $imc")
            }
        }
    }
}

@Composable
fun PantallaIngreso(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var pesoStr by remember { mutableStateOf("") }
    var alturaStr by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Calculadora de IMC",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = pesoStr,
            onValueChange = { pesoStr = it },
            label = { Text("Peso (en kg)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = alturaStr,
            onValueChange = { alturaStr = it },
            label = { Text("Altura (en metros)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (mostrarError) {
            Text(
                text = "Por favor, ingresa valores válidos",
                color = Color.Red,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                val peso = pesoStr.toFloatOrNull()
                val altura = alturaStr.toFloatOrNull()

                if (nombre.isNotBlank() && peso != null && peso > 0 && altura != null && altura > 0) {
                    mostrarError = false
                    val imc = peso / (altura * altura)
                    // Navegar a la pantalla de resultado pasando los parámetros
                    navController.navigate("resultado/${nombre.trim()}/$imc")
                } else {
                    mostrarError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Calcular", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaIngreso() {
    val navController = rememberNavController()
    PantallaIngreso(navController = navController)
}
