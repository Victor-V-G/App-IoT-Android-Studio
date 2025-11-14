package com.example.myapplication.Screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.componentestest.Componentes.Firebase.AlertasDispositivo
import com.example.componentestest.Componentes.Firebase.DispositivoData
import com.example.componentestest.Componentes.Firebase.LeerFirebase
import com.example.componentestest.Componentes.Firebase.escribirFirebase
import com.example.myapplication.Componentes.BotonesInferiores
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

// Pantalla donde se muestran los estados y mensajes relacionados con las alertas del dispositivo.
// Se leen los datos configurados en Firebase y se comparan con la corriente detectada.
@Composable
fun PantallaAlertas(navController: NavController) {

    // Contexto usado para Toast u otros componentes Android
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // ----------------------
        // HEADER DE LA PANTALLA
        // ----------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Botón para volver a "Inicio"
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
                    text = "Alertas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // ----------------------
        // CONTENIDO PRINCIPAL
        // ----------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // Composable que ejecuta la lógica de lectura y comparación
            @Composable
            fun EjecutarAlertas() {

                // Obtener usuario actual
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                // Si no hay usuario autenticado
                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                // Leer datos completos del dispositivo
                val (dispositivoDetectado, isLoadingD, errorD) =
                    LeerFirebase("sesiones/sesion_$userId/dispositivo_data", DispositivoData::class.java)

                // Leer SOLO el nodo de alertas
                val (alertaDispositivo, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/alertas_dispositivo",
                        AlertasDispositivo::class.java
                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {

                    when {

                        // Estado de carga
                        isLoading -> Text("Cargando...")

                        // Error al leer datos
                        error != null -> Text("Error: $error")

                        // Datos cargados correctamente
                        else -> {

                            // Si las alertas están activadas → analizar condiciones
                            if (alertaDispositivo?.estado == true) {

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
                                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.Notifications,
                                                contentDescription = "Establecer Alertas",
                                                modifier = Modifier
                                                    .padding(start = 30.dp)
                                                    .size(46.dp),
                                                tint = Color.Black
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            // Obtener valores desde Firebase
                                            val corriente = dispositivoDetectado?.corriente_detectada ?: 0.0
                                            val rangoMin = alertaDispositivo?.rango_minimo ?: 0.0
                                            val rangoMax = alertaDispositivo?.rango_maximo ?: 0.0

                                            // Si los rangos están sin configurar
                                            if (rangoMin == 0.0 && rangoMax == 0.0) {
                                                Text("No hay un rango mínimo y máximo establecidos")
                                            } else {

                                                // Comparaciones de rango
                                                if (corriente < rangoMin) {
                                                    Text("La corriente está por debajo del rango mínimo")
                                                }

                                                if (corriente >= rangoMin && corriente <= rangoMax) {
                                                    Text("La corriente está dentro del umbral establecido")
                                                }

                                                if (corriente > rangoMax) {
                                                    Text("La corriente está por arriba del rango máximo")
                                                }
                                            }
                                        }
                                    }
                                }

                            } else {

                                // Si la alerta está desactivada, mostrar mensaje simple
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
                                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.Notifications,
                                                contentDescription = "Establecer Alertas",
                                                modifier = Modifier
                                                    .padding(start = 30.dp)
                                                    .size(46.dp),
                                                tint = Color.Black
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            // Texto cuando la alerta está desactivada
                                            Text("Las alertas se encuentran desactivadas")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Ejecutar la función interna
            EjecutarAlertas()
        }
    }

    // Barra inferior reutilizable en toda la app
    BotonesInferiores(navController)
}
