package com.example.myapplication.Screens

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.componentestest.Componentes.Firebase.AlertasDispositivo
import com.example.componentestest.Componentes.Firebase.DispositivoData
import com.example.componentestest.Componentes.Firebase.LeerFirebase
import com.example.componentestest.Componentes.Firebase.ReleData
import com.example.componentestest.Componentes.Firebase.escribirFirebase
import com.example.myapplication.Componentes.BotonesInferiores
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

@Composable
fun PantallaDispositivo(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
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
                    text = "Ajustes",
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
            fun DispositivoAgregado() {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data",
                        DispositivoData::class.java
                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> Text("Cargando...")
                        error != null -> Text("Error: $error")
                        else -> {
                            if (dispositivoDetectado?.estado_agregado == 1) {
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
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            Image(
                                                painter = painterResource(id = R.drawable.icono_arduino),
                                                contentDescription = "Imagen de ejemplo",
                                                modifier = Modifier
                                                    .size(70.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Text(
                                                text = dispositivoDetectado.dispositivo_nombre,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            DispositivoAgregado()
        }

        //Detectar Corriente
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {
            @Composable
            fun DispositivoAgregado() {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data",
                        DispositivoData::class.java
                    )

                val (datosRele, isLoadingR, errorR) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/datos_rele",
                        ReleData::class.java
                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> Text("Cargando...")
                        error != null -> Text("Error: $error")
                        else -> {
                            if (dispositivoDetectado?.estado_agregado == 1) {
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
                                        //Corriente Detectada
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.voltaje),
                                                contentDescription = "Imagen de ejemplo",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Text(
                                                text = "Corriente Detectada: ${dispositivoDetectado.corriente_detectada.toString()}",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            DispositivoAgregado()
        }

        // Establecer alertas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            if (userId == null) {
                Text("Usuario no loggeado")
                return
            }

            val (datosRele, isLoadingR, errorR) =
                LeerFirebase(
                    "sesiones/sesion_$userId/dispositivo_data/datos_rele",
                    ReleData::class.java
                )
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
                    .padding(vertical = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.navigate("EstablecerAlertas")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Establecer Alertas",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 30.dp)
                        .size(36.dp),
                    tint = Color.Black
                )
                Text(
                    text = "Establecer Alertas",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Establecer Alertas",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 320.dp)
                        .size(24.dp),
                    tint = Color.Black
                )
            }
        }

        //Detectar Estado del relé
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {
            @Composable
            fun EstadoRele() {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data",
                        DispositivoData::class.java
                    )

                val (datosRele, isLoadingR, errorR) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/datos_rele",
                        ReleData::class.java
                    )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> Text("Cargando...")
                        error != null -> Text("Error: $error")
                        else -> {
                            if (dispositivoDetectado?.estado_agregado == 1) {
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
                                        //Estado rele
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "Estado Relé",
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                                    .size(26.dp),
                                                tint = Color.Black
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            if (datosRele?.estado_rele == false ) {
                                                Text(
                                                    text = "Estado del Relé: Apagado",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            } else {
                                                Text(
                                                    text = "Estado del Relé: Encendido",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Bold
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
            EstadoRele()
        }

        //modo de uso rele
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-10).dp),
            verticalArrangement = Arrangement.Center
        ) {
            @Composable
            fun ModoDeUso() {
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data",
                        DispositivoData::class.java
                    )

                val (alertaDispositivo, isLoadingA, errorA) =
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
                        .offset(y = (-10).dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> Text("Cargando...")
                        error != null -> Text("Error: $error")
                        else -> {
                            if (dispositivoDetectado?.estado_agregado == 1) {
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
                                        .padding(24.dp)
                                ) {

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(18.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {

                                        Text(
                                            text = "Modo de uso Relé",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )

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
                                                            "rango_minimo" to (alertaDispositivo?.rango_minimo),
                                                            "rango_maximo" to (alertaDispositivo?.rango_maximo)
                                                        ),
                                                        "datos_rele" to mapOf(
                                                            "estado_rele" to (datosRele?.estado_rele),
                                                            "modo_uso" to if (datosRele?.modo_uso == 0) 1 else 0,
                                                        )
                                                    )
                                                )
                                            }
                                        ) {
                                            Text(
                                                text = if (datosRele?.modo_uso == 0)
                                                    "Cambiar a modo automático"
                                                else
                                                    "Cambiar a modo Manual"
                                            )


                                        }

                                        if (datosRele?.modo_uso == 0) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                // BOTÓN ENCENDER / APAGAR
                                                if (datosRele.estado_rele == false) {

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
                                                                        "estado_rele" to true,
                                                                        "modo_uso" to datosRele.modo_uso
                                                                    )
                                                                )
                                                            )
                                                        },
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Text("Encender")
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
                                                                        "rango_minimo" to (alertaDispositivo?.rango_minimo),
                                                                        "rango_maximo" to (alertaDispositivo?.rango_maximo),
                                                                    ),
                                                                    "datos_rele" to mapOf(
                                                                        "estado_rele" to false,
                                                                        "modo_uso" to datosRele.modo_uso,
                                                                    )
                                                                )
                                                            )
                                                        },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = Color(0xFFD32F2F),
                                                            contentColor = Color.White
                                                        ),
                                                        modifier = Modifier.weight(1f)
                                                    ) {
                                                        Text("Apagar")
                                                    }
                                                }
                                            }
                                        } else {
                                            if (dispositivoDetectado.corriente_detectada > 8.0) {
                                                escribirFirebase(
                                                    field = "sesiones/sesion_$userId/dispositivo_data",
                                                    value = mapOf(
                                                        "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                                        "estado_agregado" to 1,
                                                        "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                                        "alertas_dispositivo" to mapOf(
                                                            "estado" to false,
                                                            "rango_minimo" to (alertaDispositivo?.rango_minimo),
                                                            "rango_maximo" to (alertaDispositivo?.rango_maximo),
                                                        ),
                                                        "datos_rele" to mapOf(
                                                            "estado_rele" to false, //apaga el rele automaticamente
                                                            "modo_uso" to datosRele?.modo_uso,
                                                        )
                                                    )
                                                )
                                            } else {
                                                escribirFirebase(
                                                    field = "sesiones/sesion_$userId/dispositivo_data",
                                                    value = mapOf(
                                                        "dispositivo_nombre" to (dispositivoDetectado?.dispositivo_nombre ?: "Desconocido"),
                                                        "estado_agregado" to 1,
                                                        "corriente_detectada" to (dispositivoDetectado?.corriente_detectada ?: 0),
                                                        "alertas_dispositivo" to mapOf(
                                                            "estado" to false,
                                                            "rango_minimo" to (alertaDispositivo?.rango_minimo),
                                                            "rango_maximo" to (alertaDispositivo?.rango_maximo),
                                                        ),
                                                        "datos_rele" to mapOf(
                                                            "estado_rele" to true, //enciende el rele automaticamente
                                                            "modo_uso" to datosRele?.modo_uso,
                                                        )
                                                    )
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
            ModoDeUso()
        }
    }
    BotonesInferiores(navController)

}
//Leer corriente (Completo)
//Establecer rangos minimos y maximos para alertas (completo)
//monitorear estado del rele (completo)
//modo de uso rele manual/automatico (completo)