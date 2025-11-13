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
import com.example.componentestest.Componentes.Firebase.ReleData

@Composable
fun PantallaEstablecerAlertas(navController: NavController) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // icono de volver
                IconButton(
                    onClick = { navController.navigate("Dispositivo") },
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

                // Texto centrado
                Text(
                    text = "Establecer Alertas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {
            @Composable
            fun VerificarEstado() {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                val (dispositivoDetectado, isLoadingD, errorD) =
                    LeerFirebase("sesiones/sesion_$userId/dispositivo_data", DispositivoData::class.java)

                val (alertaDispositivo, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/alertas_dispositivo",
                        AlertasDispositivo::class.java
                    )

                val (datosRele, isLoadingR, errorR) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/datos_rele",
                        ReleData::class.java
                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> Text("Cargando...")
                        error != null -> Text("Error: $error")
                        else -> {
                            //recuperar el estado (si es false o true)
                            val estado = alertaDispositivo?.estado ?: false
                            val textoEstado = if (estado) "Encendido" else "Apagado"

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(
                                        color = Color(0xFFEEEDED),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
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

                                        Text(
                                            text = "Alertas:",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (estado == false) {
                                            Button(
                                                onClick = {
                                                    escribirFirebase(
                                                        field = "sesiones/sesion_$userId/dispositivo_data",
                                                        value = mapOf(
                                                            "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                                            "estado_agregado" to 1,
                                                            "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                                            "alertas_dispositivo" to mapOf(
                                                                "estado" to true,
                                                                "rango_minimo" to (alertaDispositivo?.rango_minimo),
                                                                "rango_maximo" to (alertaDispositivo?.rango_maximo),
                                                            ),
                                                            "datos_rele" to mapOf(
                                                                "estado_rele" to (datosRele?.estado_rele),
                                                                "modo_uso" to (datosRele?.modo_uso),
                                                            )
                                                        )
                                                    )
                                                }
                                            ) {
                                                Text(
                                                    text = "Encender"
                                                )
                                            }
                                        } else {
                                            Button(
                                                onClick = {
                                                    escribirFirebase(
                                                        field = "sesiones/sesion_$userId/dispositivo_data",
                                                        value = mapOf(
                                                            "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                                            "estado_agregado" to 1,
                                                            "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                                            "alertas_dispositivo" to mapOf(
                                                                "estado" to false,
                                                                "rango_minimo" to (alertaDispositivo.rango_minimo),
                                                                "rango_maximo" to (alertaDispositivo.rango_maximo),
                                                            ),
                                                            "datos_rele" to mapOf(
                                                                "estado_rele" to (datosRele?.estado_rele),
                                                                "modo_uso" to (datosRele?.modo_uso),
                                                            )
                                                        )
                                                    )
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFFD32F2F),
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Text(
                                                    text = "Apagar"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            VerificarEstado()
        }

        // Establecer rangos mínimos y máximos de corriente para alerta
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {
            @Composable
            fun EstablecerEstado() {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                val (dispositivoDetectado, isLoadingD, errorD) =
                    LeerFirebase("sesiones/sesion_$userId/dispositivo_data", DispositivoData::class.java)

                val (alertaDispositivo, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/alertas_dispositivo",
                        AlertasDispositivo::class.java
                    )

                val (datosRele, isLoadingR, errorR) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/datos_rele",
                        ReleData::class.java
                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> Text("Cargando...")
                        error != null -> Text("Error: $error")
                        else -> {
                            // Recuperar el estado (si es false o true)
                            val estado = alertaDispositivo?.estado ?: false
                            val textoEstado = if (estado) "Encendido" else "Apagado"

                            // Estado local del campo de rango mínimo y maximo
                            var rangoMinimo by rememberSaveable { mutableStateOf("") }
                            var rangoMaximo by rememberSaveable { mutableStateOf("") }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(
                                        color = Color(0xFFEEEDED),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {

                                    Text(
                                        text = "Establecer rangos de alertas",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Rango minimo establecido: ${alertaDispositivo?.rango_minimo}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Rango maximo establecido: ${alertaDispositivo?.rango_maximo}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Campo para rango mínimo de corriente
                                    OutlinedTextField(
                                        value = rangoMinimo,
                                        onValueChange = { input ->
                                            // Permite solo números y punto decimal
                                            if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                                rangoMinimo = input
                                            }
                                        },
                                        label = { Text("Rango mínimo de corriente (A)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        modifier = Modifier.fillMaxWidth(0.9f)
                                    )

                                    OutlinedTextField(
                                        value = rangoMaximo,
                                        onValueChange = { input ->
                                            // Permite solo números y punto decimal
                                            if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                                rangoMaximo = input
                                            }
                                        },
                                        label = { Text("Rango maximo de corriente (A)") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        modifier = Modifier.fillMaxWidth(0.9f)
                                    )

                                    Button(
                                        onClick = {
                                            val min = rangoMinimo.toDoubleOrNull()
                                            val max = rangoMaximo.toDoubleOrNull()

                                            when {
                                                min == null || max == null -> {
                                                    Toast.makeText(context, "Por favor ingresa ambos rangos numéricos", Toast.LENGTH_SHORT).show()
                                                }

                                                min >= max -> {
                                                    Toast.makeText(context, "El rango mínimo no puede ser mayor o igual al rango máximo", Toast.LENGTH_SHORT).show()
                                                }

                                                else -> {
                                                    escribirFirebase(
                                                        field = "sesiones/sesion_$userId/dispositivo_data",
                                                        value = mapOf(
                                                            "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                                            "estado_agregado" to 1,
                                                            "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                                            "alertas_dispositivo" to mapOf(
                                                                "estado" to (alertaDispositivo?.estado),
                                                                "rango_minimo" to min,
                                                                "rango_maximo" to max
                                                            ),
                                                            "datos_rele" to mapOf(
                                                                "estado_rele" to (datosRele?.estado_rele),
                                                                "modo_uso" to (datosRele?.modo_uso),
                                                            )
                                                        )
                                                    )
                                                    Toast.makeText(context, "Rangos establecidos correctamente", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    ) {
                                        Text(text = "Establecer")
                                    }

                                    //Restablecer rangos
                                    Button(
                                        onClick = {
                                            escribirFirebase(
                                                field = "sesiones/sesion_$userId/dispositivo_data",
                                                value = mapOf(
                                                    "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                                    "estado_agregado" to 1,
                                                    "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                                    "alertas_dispositivo" to mapOf(
                                                        "estado" to (alertaDispositivo?.estado),
                                                        "rango_minimo" to (0.0),
                                                        "rango_maximo" to (0.0),
                                                    ),
                                                    "datos_rele" to mapOf(
                                                        "estado_rele" to (datosRele?.estado_rele),
                                                        "modo_uso" to (datosRele?.modo_uso),
                                                    )
                                                )
                                            )
                                            rangoMinimo = ""
                                            rangoMaximo = ""
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFD32F2F),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = "Restablecer")

                                    }
                                }
                            }
                        }
                    }
                }
            }

            EstablecerEstado()
        }

    }

    BotonesInferiores(navController)
}