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

@Composable
fun PantallaAlertas(navController: NavController) {

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

                // Texto centrado
                Text(
                    text = "Alertas",
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
            fun EjecutarAlertas() {
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
                            if (alertaDispositivo?.estado == true) {
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

                                            val corriente = dispositivoDetectado?.corriente_detectada ?: 0.0
                                            val rangoMin = alertaDispositivo?.rango_minimo ?: 0.0
                                            val rangoMax = alertaDispositivo?.rango_maximo ?: 0.0

                                            if (rangoMin == 0.0 && rangoMax == 0.0) {
                                                Text(text = "No hay un rango mínimo y máximo establecidos")
                                            } else {
                                                if (corriente < rangoMin) {
                                                    Text(text = "La corriente del dispositivo está por debajo del rango mínimo establecido")
                                                }

                                                if (corriente >= rangoMin && corriente <= rangoMax) {
                                                    Text(text = "La corriente del dispositivo está en el umbral de los rangos establecidos")
                                                }

                                                if (corriente > rangoMax) {
                                                    Text(text = "La corriente del dispositivo está por arriba del rango máximo establecido")
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
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

                                            Text(text = "Las alertas se encuentran desactivadas")

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            EjecutarAlertas()
        }
    }

    BotonesInferiores(navController)
}