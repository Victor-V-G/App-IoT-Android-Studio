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

// ---------------------------------------------------------------------
// PANTALLA PRINCIPAL DE AJUSTES DEL DISPOSITIVO
//
// Esta pantalla muestra:
// - Información del dispositivo agregado
// - Corriente detectada
// - Estado del relé
// - Modo de uso del relé (manual / automático)
// - Acceso a pantalla de configuraciones de alerta
//
// Toda la información se obtiene desde Firebase en tiempo real con LeerFirebase(),
// el cual devuelve (dato, isLoading, error).
// ---------------------------------------------------------------------
@Composable
fun PantallaDispositivo(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top
    ) {

        // -------------------------------------------------------------
        // HEADER DE LA PANTALLA
        // Contiene un botón de volver y el título "Ajustes"
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón para volver a Inicio
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
                    text = "Ajustes",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // ---------------------------------------------------------------------
        // SECCIÓN 1: DISPOSITIVO AGREGADO
        // Muestra la foto y el nombre del dispositivo si estado_agregado == 1
        // ---------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            verticalArrangement = Arrangement.Center
        ) {

            @Composable
            fun DispositivoAgregado() {

                // Obtener usuario actual
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                // Validar sesión
                if (userId == null) {
                    Text("Usuario no loggeado")
                    return
                }

                // Leer información del dispositivo desde Firebase
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

                    // Lógica visible dependiendo del estado de carga
                    when {
                        isLoading -> Text("Cargando...") // esperando datos
                        error != null -> Text("Error: $error") // error en Firebase

                        else -> {
                            // Mostrar solo si el dispositivo ya fue agregado
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

                                        // Mostrar foto + nombre del dispositivo
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.icono_arduino),
                                                contentDescription = "Imagen Arduino",
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

        // ---------------------------------------------------------------------
        // SECCIÓN 2: CORRIENTE DETECTADA
        // Muestra la corriente actual reportada por el sensor
        // ---------------------------------------------------------------------
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

                // Leer dispositivo
                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data",
                        DispositivoData::class.java
                    )

                // Leer datos del relé
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

                                // Caja de datos de corriente
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

                                        // Fila corriente detectada
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            Image(
                                                painter = painterResource(id = R.drawable.voltaje),
                                                contentDescription = "Voltaje",
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Text(
                                                text = "Corriente Detectada: ${dispositivoDetectado.corriente_detectada}",
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

        // ---------------------------------------------------------------------
        // SECCIÓN 3: BOTÓN PARA IR A ESTABLECER ALERTAS
        // Caja clickeable completa → navega a la pantalla de alertas
        // ---------------------------------------------------------------------
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

            // Leer datos del relé (no se muestran aquí, pero se usan)
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
                    .clickable( // Caja completa clickeable
                        indication = null, // sin animación ripple
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.navigate("EstablecerAlertas")
                    },
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Alertas",
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
                    contentDescription = "Ir a alertas",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 320.dp)
                        .size(24.dp),
                    tint = Color.Black
                )
            }
        }

        // ---------------------------------------------------------------------
        // SECCIÓN 4: ESTADO DEL RELÉ
        // Muestra "Apagado" o "Encendido" según el dato almacenado en Firebase
        // ---------------------------------------------------------------------
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

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "Info Rele",
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                                    .size(26.dp),
                                                tint = Color.Black
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            // Estado apagado o encendido
                                            if (datosRele?.estado_rele == false) {
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

        // ---------------------------------------------------------------------
        // SECCIÓN 5: MODO DE USO DEL RELÉ
        // Permite cambiar entre:
        // - modo manual → usuario controla ON/OFF
        // - modo automático → lógica automática en función de la corriente
        //
        // Esta es la sección más compleja por el manejo de Firebase.
        // ---------------------------------------------------------------------
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

                // Leer datos del dispositivo
                val (dispositivoDetectado, isLoading, error) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data",
                        DispositivoData::class.java
                    )

                // Leer alertas configuradas
                val (alertaDispositivo, isLoadingA, errorA) =
                    LeerFirebase(
                        "sesiones/sesion_$userId/dispositivo_data/alertas_dispositivo",
                        AlertasDispositivo::class.java
                    )

                // Leer datos del relé
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

                                // Caja contenedora de TODA la lógica de modo
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

                                        // Título
                                        Text(
                                            text = "Modo de uso Relé",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )

                                        // BOTÓN PARA CAMBIAR MODO (manual / automático)
                                        Button(
                                            onClick = {

                                                // -----------------------------------------------
                                                // CAMBIO DE MODO DEL RELÉ
                                                //
                                                // modo_uso:
                                                // 0 → manual
                                                // 1 → automático
                                                //
                                                // Se invierte el valor en Firebase:
                                                // si es 0 pasa a 1, si es 1 pasa a 0.
                                                //
                                                // Importante:
                                                // escribirFirebase() sobrescribe todo el objeto,
                                                // por eso se reconstruye toda la estructura exacta.
                                                // -----------------------------------------------
                                                escribirFirebase(
                                                    field = "sesiones/sesion_$userId/dispositivo_data",
                                                    value = mapOf(
                                                        "dispositivo_nombre" to (dispositivoDetectado.dispositivo_nombre),
                                                        "estado_agregado" to 1,
                                                        "corriente_detectada" to dispositivoDetectado.corriente_detectada,
                                                        "alertas_dispositivo" to mapOf(
                                                            "estado" to alertaDispositivo?.estado,
                                                            "rango_minimo" to alertaDispositivo?.rango_minimo,
                                                            "rango_maximo" to alertaDispositivo?.rango_maximo
                                                        ),
                                                        "datos_rele" to mapOf(
                                                            "estado_rele" to datosRele?.estado_rele,
                                                            "modo_uso" to if (datosRele?.modo_uso == 0) 1 else 0
                                                        )
                                                    )
                                                )
                                            }
                                        ) {

                                            // Texto dinámico del botón
                                            Text(
                                                text =
                                                    if (datosRele?.modo_uso == 0)
                                                        "Cambiar a modo automático"
                                                    else
                                                        "Cambiar a modo Manual"
                                            )
                                        }

                                        // ---------------------------------------------------------
                                        // LÓGICA DEL MODO MANUAL:
                                        // El usuario controla directamente el estado del relé.
                                        // ---------------------------------------------------------
                                        if (datosRele?.modo_uso == 0) {

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {

                                                // Si el relé está APAGADO → mostrar botón Encender
                                                if (datosRele.estado_rele == false) {

                                                    Button(
                                                        onClick = {

                                                            // Se enciende el relé manualmente
                                                            escribirFirebase(
                                                                field = "sesiones/sesion_$userId/dispositivo_data",
                                                                value = mapOf(
                                                                    "dispositivo_nombre" to dispositivoDetectado.dispositivo_nombre,
                                                                    "estado_agregado" to 1,
                                                                    "corriente_detectada" to dispositivoDetectado.corriente_detectada,
                                                                    "alertas_dispositivo" to mapOf(
                                                                        "estado" to true,
                                                                        "rango_minimo" to alertaDispositivo?.rango_minimo,
                                                                        "rango_maximo" to alertaDispositivo?.rango_maximo
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

                                                }
                                                // Si está ENCENDIDO → mostrar botón Apagar
                                                else {

                                                    Button(
                                                        onClick = {

                                                            // Se apaga manualmente
                                                            escribirFirebase(
                                                                field = "sesiones/sesion_$userId/dispositivo_data",
                                                                value = mapOf(
                                                                    "dispositivo_nombre" to dispositivoDetectado.dispositivo_nombre,
                                                                    "estado_agregado" to 1,
                                                                    "corriente_detectada" to dispositivoDetectado.corriente_detectada,
                                                                    "alertas_dispositivo" to mapOf(
                                                                        "estado" to false,
                                                                        "rango_minimo" to alertaDispositivo?.rango_minimo,
                                                                        "rango_maximo" to alertaDispositivo?.rango_maximo
                                                                    ),
                                                                    "datos_rele" to mapOf(
                                                                        "estado_rele" to false,
                                                                        "modo_uso" to datosRele.modo_uso
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

                                        }

                                        // ---------------------------------------------------------
                                        // LÓGICA DEL MODO AUTOMÁTICO
                                        // El relé se controla SOLO según la corriente detectada
                                        //
                                        // Reglas aplicadas:
                                        // - Si corriente > 8.0 → relé se APAGA automáticamente
                                        // - Si corriente ≤ 8.0 → relé se ENCIENDE automáticamente
                                        //
                                        // Esto actúa como protección por sobrecorriente.
                                        // ---------------------------------------------------------
                                        else {

                                            // Regla de automatización:
                                            if (dispositivoDetectado.corriente_detectada > 8.0) {

                                                // Apaga automáticamente el relé
                                                escribirFirebase(
                                                    field = "sesiones/sesion_$userId/dispositivo_data",
                                                    value = mapOf(
                                                        "dispositivo_nombre" to dispositivoDetectado.dispositivo_nombre,
                                                        "estado_agregado" to 1,
                                                        "corriente_detectada" to dispositivoDetectado.corriente_detectada,
                                                        "alertas_dispositivo" to mapOf(
                                                            "estado" to false,
                                                            "rango_minimo" to alertaDispositivo?.rango_minimo,
                                                            "rango_maximo" to alertaDispositivo?.rango_maximo
                                                        ),
                                                        "datos_rele" to mapOf(
                                                            "estado_rele" to false,
                                                            "modo_uso" to datosRele?.modo_uso
                                                        )
                                                    )
                                                )

                                            } else {

                                                // Enciende automáticamente el relé
                                                escribirFirebase(
                                                    field = "sesiones/sesion_$userId/dispositivo_data",
                                                    value = mapOf(
                                                        "dispositivo_nombre" to dispositivoDetectado.dispositivo_nombre,
                                                        "estado_agregado" to 1,
                                                        "corriente_detectada" to dispositivoDetectado.corriente_detectada,
                                                        "alertas_dispositivo" to mapOf(
                                                            "estado" to false,
                                                            "rango_minimo" to alertaDispositivo?.rango_minimo,
                                                            "rango_maximo" to alertaDispositivo?.rango_maximo
                                                        ),
                                                        "datos_rele" to mapOf(
                                                            "estado_rele" to true,
                                                            "modo_uso" to datosRele?.modo_uso
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

    // Barra inferior reutilizable en toda la app
    BotonesInferiores(navController)
}
