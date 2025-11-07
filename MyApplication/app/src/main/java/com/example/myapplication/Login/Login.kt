package com.example.myapplication.Login

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myapplication.Registro.RegistroUsuario
import com.example.myapplication.ViewModel.UsuarioViewModel
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp


@Composable
fun Login (navController: NavController, usuarioViewModel: UsuarioViewModel) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = { Text("Ingrese su username") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {Text("Ingrese su password")},
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                if (usuarioViewModel.usuarios.containsKey(username)) {
                    val verificarUsername = usuarioViewModel.usuarios[username]
                    val verificarPassword = usuarioViewModel.usuarios[password]

                    if (verificarUsername == username) {
                        if (verificarPassword == password) {
                            println("INICIO SESION CORRECTAMENTE")
                        } else {
                            println("Credenciales Incorrectas")
                        }
                    }
                } else {
                    println("Este usuario no existe")
                }
            }
        ) { }

        Button(
            onClick = {
                navController.navigate("Registro")
            }
        ) {
            Text("Ir a Registro")
        }

    }
}