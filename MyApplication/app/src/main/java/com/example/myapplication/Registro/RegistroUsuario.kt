package com.example.myapplication.Registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.myapplication.ViewModel.UsuarioViewModel

//Variables mutables (var), puede cambiar su valor
//Variables inmutables (val), no puede cambiar su valor

@Composable
fun RegistroUsuario (navController: NavController, usuarioViewModel: UsuarioViewModel) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmarPasswordError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
            verticalArrangement = Arrangement.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = {
                navController.navigate("Login")
            }
        ) {Text(
            text = "<- Volver atras",
            style = MaterialTheme.typography.bodySmall
        ) }

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = false
            },
            label = { Text("Ingrese su username") },
            modifier = Modifier.fillMaxWidth(),
            isError = usernameError
        )
        if (usernameError == true) {
            Text(
                text = "El username no puede quedar vacio",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
           },
            label = {Text("Ingrese su password")},
            modifier = Modifier.fillMaxWidth(),
            isError = passwordError,
        )
        if (passwordError == true) {
            Text(
                text = "La password no puede quedar vacia",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmarPassword,
            onValueChange = {
                confirmarPassword = it
                confirmarPasswordError = false
            },
            label = {Text("Confirme su password")},
            modifier = Modifier.fillMaxWidth(),
            isError = confirmarPasswordError
        )
        if (confirmarPasswordError == true) {
            Text(
                text = "La confirmacion no puede quedar vacia",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                usernameError = false
                passwordError = false
                confirmarPasswordError = false
                mensajeError = ""

                when {
                    username.isBlank() -> usernameError = true
                    password.isBlank() -> passwordError = true
                    confirmarPassword.isBlank() -> confirmarPasswordError = true
                    password != confirmarPassword -> {
                        mensajeError = "Las password no coinciden"
                        password = ""
                        confirmarPassword = ""
                    }
                    else -> {
                        usuarioViewModel.registrarUsuario(username, password)
                        println("Registro guardado: ${usuarioViewModel.usuarios}")
                        navController.navigate("Login")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Usuario")
        }

        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensajeError,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}