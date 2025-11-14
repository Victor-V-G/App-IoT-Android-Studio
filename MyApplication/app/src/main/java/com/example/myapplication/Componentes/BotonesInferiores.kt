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

// Componente que dibuja una barra inferior reutilizable con navegación.
// Contiene 3 botones principales: Inicio, Alertas y Cerrar sesión.
// Este componente puede usarse en cualquier pantalla para mantener una navegación uniforme.
@Composable
fun BotonesInferiores(navController: NavController) {

    // Instancia de la clase que maneja acciones de autenticación, como cerrar sesión.
    val authenticationClass = AuthenticationFirebase()

    // Estado que controla si el diálogo de confirmación debe mostrarse.
    // show = true  → mostrar diálogo
    // show = false → ocultar diálogo
    var show by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom // Pegamos todo al fondo de la pantalla.
    ) {

        // Barra inferior principal: contiene los iconos y textos de navegación
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFECECEC)) // Fondo gris claro
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(50.dp), // Separación entre botones
                verticalAlignment = Alignment.CenterVertically
            ) {

                // -----------------------------
                // BOTÓN: HOME
                // -----------------------------
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(
                        onClick = { navController.navigate("Inicio") }, // Navegar a pantalla de inicio
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

                // -----------------------------
                // BOTÓN: ALERTAS
                // -----------------------------
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(
                        onClick = { navController.navigate("Alertas") }, // Navegar a la pantalla de alertas
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

                // -----------------------------
                // BOTÓN: CERRAR SESIÓN
                // -----------------------------
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(
                        onClick = { show = true }, // Mostrar diálogo de confirmación
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

        // Segunda barra (decorativa): agrega una franja más clara debajo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1)) // Gris aún más claro
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Vacío. Solo cumple un propósito visual.
        }

        // Diálogo de confirmación para cerrar sesión
        CerrarSesionDialog(
            show = show,               // Mostrar diálogo si show = true
            onDismiss = { show = false }, // Cerrar diálogo al cancelar
            onConfirm = {
                // Al confirmar, primero cerramos sesión en FirebaseAuth
                authenticationClass.cerrarSesion()

                // Después navegamos a la pantalla de Login
                navController.navigate("Login") {

                    // popUpTo elimina la pantalla Inicio del historial
                    // inclusive = true → evita que el usuario pueda volver con el botón "atrás"
                    popUpTo("Inicio") { inclusive = true }

                    // Evita crear múltiples instancias de Login en el stack
                    launchSingleTop = true
                }
            }
        )
    }
}


// Composable que muestra un diálogo de alerta para confirmar cierre de sesión.
// show = true → el diálogo se muestra
// show = false → no se muestra
@Composable
fun CerrarSesionDialog(
    show: Boolean,
    onDismiss: () -> Unit,  // Acción cuando se cancela
    onConfirm: () -> Unit   // Acción cuando se confirma
) {

    // Solo dibuja el diálogo si show = true
    if (show) {

        AlertDialog(
            onDismissRequest = onDismiss, // Se ejecuta cuando el usuario toca fuera del diálogo

            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Confirmar") // Botón para cerrar sesión
                }
            },

            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar") // Botón para cancelar la acción
                }
            },

            title = { Text("Cerrar sesión") },
            text = { Text("¿Está seguro que desea cerrar la sesión?") }
        )
    }
}
