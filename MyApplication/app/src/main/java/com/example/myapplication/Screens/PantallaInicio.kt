package com.example.myapplication.Screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.componentestest.Componentes.Firebase.DispositivoData
import com.example.componentestest.Componentes.Firebase.LeerFirebase
import com.example.componentestest.Componentes.Firebase.escribirFirebase
import com.example.myapplication.Componentes.BotonesInferiores
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

// -----------------------------------------------------------------------------
// PANTALLA PRINCIPAL (HOME)
// Muestra:
//   - correo del usuario logueado
//   - dispositivo agregado (si existe)
//   - botón para añadir un nuevo dispositivo
//   - barra inferior de navegación
//
// Contiene lógica para:
//   - evitar navegación hacia atrás: cierra la app en vez de volver a Login
//   - leer desde Firebase el dispositivo agregado
//   - navegar a PantallaDispositivo y PantallaAgregar
// -----------------------------------------------------------------------------
@Composable
fun PantallaInicio(navController: NavController) {

    // Obtener la Activity para poder cerrar la app
    val activity = LocalContext.current as? Activity

    // -------------------------------------------------------------------------
    // BACKHANDLER - evita volver al Login al presionar "Atrás"
    // Cuando se está en la pantalla Inicio, la app debería cerrarse.
    // finishAffinity() = cierra todas las activities de la app.
    // exitProcess(0) = garantiza cierre completo de la ejecución.
    // -------------------------------------------------------------------------
    BackHandler {
        activity?.finishAffinity()
        exitProcess(0)
    }

    // -------------------------------------------------------------------------
    // CONTENEDOR PRINCIPAL
    // -------------------------------------------------------------------------
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // ---------------------------------------------------------------------
        // HEADER SUPERIOR - saluda al usuario
        // ---------------------------------------------------------------------
        Column(
            modifier = Modifier.padding(top = 40.dp),
            verticalArrangement = Arrangement.Top
        ) {

            // Se obtiene usuario actual de Firebase
            val user = FirebaseAuth.getInstance().currentUser
            val userEmail = user?.email // correo del usuario logueado

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                // Mostrar email
                Text(
                    text = "Bienvenido: $userEmail",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }

        // ---------------------------------------------------------------------
        // SECCIÓN DISPOSITIVO AGREGADO
        // ---------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // Título “Dispositivos Agregados”
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Dispositivos Agregados:",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }

            // -----------------------------------------------------------------
            // COMPOSABLE INTERNO PARA MOSTRAR EL DISPOSITIVO AGREGADO
            //
            // Lógica:
            // 1) Lee de Firebase: sesiones/sesion_UID/dispositivo_data
            // 2) Si estado_agregado == 1 significa que ya existe un dispositivo
            // 3) Lo muestra en una card
            // 4) Click → Navega a PantallaDispositivo
            // -----------------------------------------------------------------
            @Composable
            fun DispositivoAgregado() {

                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                // Se leen los datos del nodo dispositivo_data
                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase("sesiones/sesion_$userId/dispositivo_data", DispositivoData::class.java)

                // -----------------------------------------------------------------
                // CARD que representa el dispositivo
                // clickable → abre PantallaDispositivo
                // -----------------------------------------------------------------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .clickable(
                            indication = null, // elimina el ripple
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // Navegar al panel de ajustes del dispositivo
                            navController.navigate("Dispositivo")
                        },
                    contentAlignment = Alignment.Center
                ) {

                    when {

                        isLoading -> Text("Cargando...")

                        error != null -> Text("Error: $error")

                        else -> {

                            // Si ya existe un dispositivo asociado (estado_agregado == 1)
                            if (dispositivoDetectado?.estado_agregado == 1) {

                                // -------------------------------------------------------------
                                // CARD VISUAL DEL DISPOSITIVO
                                // -------------------------------------------------------------
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .background(Color(0xFFEEEDED), RoundedCornerShape(16.dp))
                                        .border(2.dp, Color.Transparent, RoundedCornerShape(16.dp))
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            // Imagen representativa del dispositivo (Arduino)
                                            Image(
                                                painter = painterResource(id = R.drawable.icono_arduino),
                                                contentDescription = "Imagen de ejemplo",
                                                modifier = Modifier
                                                    .size(100.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            // Nombre del dispositivo
                                            Text(
                                                text = dispositivoDetectado.dispositivo_nombre,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        // Flecha que indica navegación
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = "Acceder",
                                            tint = Color.Black,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Ejecutar el composable del dispositivo
            DispositivoAgregado()
        }

        // ---------------------------------------------------------------------
        // SECCIÓN PARA AGREGAR NUEVO DISPOSITIVO
        // ---------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color(0xFFEEEDED), RoundedCornerShape(16.dp))
                    .padding(60.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
                    .clickable {
                        // Navega a la pantalla para detectar/agregar nuevo dispositivo
                        navController.navigate("Agregar")
                    },
                contentAlignment = Alignment.Center
            ) {

                // Icono "+"
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Barra inferior reutilizable
        BotonesInferiores(navController)
    }
}

// victorvicencio932@gmail.com
// vitoco2005
