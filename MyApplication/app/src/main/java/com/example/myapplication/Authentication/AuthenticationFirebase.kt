package com.example.myapplication.Authentication

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class AuthenticationFirebase () {

    @Composable
    fun MenuLogin(navController: NavController) {

        var email by remember { mutableStateOf("") }
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
                value = email,
                onValueChange = { email = it },
                label = { Text("Ingrese su correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Ingrese su contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password.isNotEmpty()) {
                        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            loginFirebase(navController, email, password)
                        } else {
                            println("Formato de correo electrónico incorrecto")
                        }
                    } else {
                        println("Ingrese su contraseña")
                    }
                }
            ) {
                Text("Ingresar")
            }
        }
    }

    //Funcion sacada de https://firebase.google.com/docs/auth/android/start?hl=es-419#sign_in_existing_users
    // y adaptada a mis requerimientos y logica
    private fun loginFirebase(navController: NavController, email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("Inicio")
                } else {
                    println("Error al iniciar sesión: ${task.exception?.message}")
                }
            }
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            println("Usuario deslogueado correctamente")
        } else {
            println("Usuario sigue logueado: ${user.email}")
        }
    }

//    fun obtenerUsuarioLoggeado(){
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            // User is signed in
//            println("Se loggeo")
//            println(user)
//        } else {
//            // No user is signed in
//            println("No esta loggeado")
//        }
//    }
}
