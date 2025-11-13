package com.example.myapplication.Componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
                .background(Color(0xFFECECEC))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Botón Home
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { navController.navigate("Inicio") },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Menu principal",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "Menú principal",
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }

                // Botón Alertas
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { navController.navigate("Alertas") },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alertas",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "Alertas",
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }

                // Botón Salir
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = { show = true },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = "Cerrar sesión",
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }
            }
        }

        // Línea inferior gris claro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1))
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
