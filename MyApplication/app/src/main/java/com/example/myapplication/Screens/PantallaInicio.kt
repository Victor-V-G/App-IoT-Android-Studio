package com.example.myapplication.Screens

import android.app.Activity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.example.componentestest.Componentes.Firebase.DispositivoData
import com.example.componentestest.Componentes.Firebase.LeerFirebase
import com.example.componentestest.Componentes.Firebase.escribirFirebase
import com.example.myapplication.Componentes.BotonesInferiores
import com.google.firebase.auth.FirebaseAuth
import kotlin.system.exitProcess

@Composable
fun PantallaInicio(navController: NavController) {

    val authenticationClass = AuthenticationFirebase()

    var show by rememberSaveable { mutableStateOf(false) }

    val activity = LocalContext.current as? Activity

    BackHandler {
        activity?.finishAffinity()
        exitProcess(0)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Parte superior
        Column(
            modifier = Modifier.padding(top = 40.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Pantalla de inicio")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
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
                    LeerFirebase("sesiones/sesion_$userId/dispositivo_data", DispositivoData::class.java)

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
                                        .padding(60.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dispositivoDetectado.dispositivo_nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            DispositivoAgregado()
        }

        // Ingresar Dispositivo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color(0xFFEEEDED), shape = RoundedCornerShape(16.dp))
                    .padding(60.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 24.dp)
                    .clickable{
                        navController.navigate("Agregar")
                    },
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        BotonesInferiores(navController)
    }
}


// victorvicencio932@gmail.com
// vitoco2005