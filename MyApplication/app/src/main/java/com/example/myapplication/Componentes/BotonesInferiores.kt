package com.example.myapplication.Componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Authentication.AuthenticationFirebase

@Composable
fun BotonesInferiores(navController: NavController) {

    val authenticationClass = AuthenticationFirebase()
    var show by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Barra inferior gris oscuro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF868484))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Home
                Button(
                    onClick = { navController.navigate("Inicio") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Home", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    }
                }

                // Botón Alertas
                Button(
                    onClick = { /* Acción futura */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alertas",
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Alertas", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    }
                }

                // Botón Salir
                Button(
                    onClick = { show = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Cerrar Sesión",
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Salir", fontSize = MaterialTheme.typography.labelSmall.fontSize)
                    }
                }
            }
        }

        // Línea inferior gris claro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE7E7E7))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {}

        // Diálogo de confirmación
        CerrarSesionDialog(
            show = show,
            onDismiss = { show = false },
            onConfirm = {
                authenticationClass.cerrarSesion()
                navController.navigate("Login") {
                    popUpTo("Inicio") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun CerrarSesionDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Está seguro que desea cerrar la sesión?") }
        )
    }
}
