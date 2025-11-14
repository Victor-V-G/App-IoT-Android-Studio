package com.example.myapplication.Screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.Authentication.AuthenticationFirebase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.componentestest.Componentes.Firebase.DispositivoData
import com.example.componentestest.Componentes.Firebase.LeerFirebase
import com.example.componentestest.Componentes.Firebase.escribirFirebase
import com.example.myapplication.Componentes.BotonesInferiores
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R



// Pantalla donde se muestra la interfaz para "Agregar Dispositivo"
// Se compone de un header, un contenedor y el widget para detectar el dispositivo.
@Composable
fun PantallaAgregar(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // --------------------------
        // HEADER DE LA PANTALLA
        // --------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Botón "volver"
                IconButton(
                    onClick = { navController.navigate("Inicio") },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Título centrado
                Text(
                    text = "Agregar Dispositivo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // CONTENIDO PRINCIPAL
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // Caja decorativa que contiene la lógica de detectar el dispositivo
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color(0xFFEEEDED), shape = RoundedCornerShape(16.dp))
                    .padding(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                DetectarDispositivo(navController)
            }
        }
    }

    // Barra inferior reutilizable
    BotonesInferiores(navController)
}



// Función encargada de leer los datos del dispositivo desde Firebase
// y permitir al usuario agregarlo si corresponde.
@Composable
private fun DetectarDispositivo(navController : NavController) {

    // Obtener usuario actual
    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    // Contexto para mostrar Toasts
    val context = LocalContext.current

    // Si no hay usuario autenticado
    if (userId == null) {
        Text("Usuario no loggeado")
        return
    }

    // Leer datos desde Firebase usando tu lector genérico
    val (dispositivoDetectado, isLoading, error) =
        LeerFirebase("sesiones/sesion_$userId/dispositivo_data", DispositivoData::class.java)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        when {

            // Estado de carga
            isLoading -> Text("Cargando...")

            // Error al obtener datos
            error != null -> Text("Error: $error")

            // Datos cargados correctamente
            else -> {

                // Imagen decorativa o representativa del dispositivo detectado
                Image(
                    painter = painterResource(id = R.drawable.icono_arduino),
                    contentDescription = "Imagen de ejemplo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar el nombre del dispositivo leído de Firebase
                Text(
                    text = "Dispositivo: ${dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botón para agregar el dispositivo
                Button(
                    onClick = {

                        // Estado actual (0 = no agregado, 1 = agregado)
                        val estadoAgregado = dispositivoDetectado?.estado_agregado ?: 0

                        // Si ya fue agregado antes → mostrar aviso
                        if (estadoAgregado == 1) {

                            Toast.makeText(
                                context,
                                "Este dispositivo ya está agregado",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            // Guardar datos en Firebase como "agregado"
                            escribirFirebase(
                                field = "sesiones/sesion_$userId/dispositivo_data",
                                value = mapOf(
                                    "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                    "estado_agregado" to 1,
                                    "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                    "alertas_dispositivo" to mapOf(
                                        "estado" to false,
                                        "rango_minimo" to 0.0,
                                        "rango_maximo" to 0.0
                                    ),
                                    "datos_rele" to mapOf(
                                        "estado_rele" to false,
                                        "modo_uso" to 0
                                    )
                                )
                            )

                            // Volver a la pantalla de inicio
                            navController.navigate("Inicio")

                            Toast.makeText(
                                context,
                                "Dispositivo agregado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Agregar")
                }
            }
        }
    }
}
