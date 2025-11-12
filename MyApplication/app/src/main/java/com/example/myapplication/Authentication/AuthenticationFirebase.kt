package com.example.myapplication.Authentication

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import android.util.Log
import com.google.firebase.database.*

class AuthenticationFirebase () {

    @Composable
    fun MenuLogin(navController: NavController) {

        var email by remember { mutableStateOf(value = "") }
        var password by remember { mutableStateOf(value = "") }

        var visibleInput by remember { mutableStateOf(value = false) }

        var emailError by remember { mutableStateOf(value = false) }
        var passwordError by remember { mutableStateOf(value = false) }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                fontSize = 50.sp
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = email.isBlank()
                },
                label = { Text(text = "Ingrese su correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailError
            )
            if (emailError == true) {
                Text(
                    text = "El correo no puede quedar vacio",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(height = 16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = password.isBlank()
                },
                label = { Text(text = "Ingrese su contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (visibleInput) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { visibleInput = !visibleInput }) {
                        Text(
                            text = if (visibleInput) {
                                "Ocultar"
                            } else {"Ver"}
                        )
                    }
                },
                singleLine = true,
                isError = passwordError
            )
            if (passwordError == true) {
                Text(
                    text = "La contraseña no puede quedar vacia",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(height = 16.dp))

            Button(
                onClick = {

                    emailError = email.isBlank()
                    passwordError = password.isBlank()

                    if (password.isNotEmpty()) {
                        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            loginFirebase(navController, context, email, password)
                        } else {
                            Toast.makeText(context, "Correo invalido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Ingresar")
            }
        }
    }

    private fun crearSesionUsuario(userId: String) {
        val database = Firebase.database
        val sesionRef = database.getReference("sesiones/sesion_$userId/dispositivo_data")

        sesionRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val datosIniciales = mapOf(
                        "dispositivo_nombre" to "Arduino WEMOS D1",
                        "estado_agregado" to 0,
                        "corriente_detectada" to 1.0,
                        "alertas_dispositivo" to mapOf(
                            "estado" to false,
                            "rango_minimo" to 0.0,
                            "rango_maximo" to 0.0
                        )
                    )
                    sesionRef.setValue(datosIniciales)
                        .addOnSuccessListener {
                            println("Sesión creada para el usuario $userId")
                        }
                        .addOnFailureListener { error ->
                            println("Error al crear sesión: ${error.message}")
                        }
                } else {
                    println("Sesión existente, no se crea duplicado")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al leer sesión: ${error.message}")
            }
        })
    }


    //Funcion sacada de https://firebase.google.com/docs/auth/android/start?hl=es-419#sign_in_existing_users
    // y adaptada a mis requerimientos y logica
    private fun loginFirebase(navController: NavController, context: android.content.Context, email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid ?: ""

                    crearSesionUsuario(userId)

                    navController.navigate("Inicio")
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    val exception = task.exception
                    val message = when (exception) {
                        is com.google.firebase.auth.FirebaseAuthInvalidUserException -> {
                            "Las credenciales ingresadas no estan registradas"
                        }
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                            "El correo o la contraseña son invalidas"
                        }
                        else -> {
                            "Error al iniciar sesión: ${exception?.localizedMessage ?: "Intente nuevamente"}"
                        }
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
