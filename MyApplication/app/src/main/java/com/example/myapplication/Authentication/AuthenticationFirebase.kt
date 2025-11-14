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
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.database.*

// Clase encargada de mostrar el login, validar credenciales,
// autenticar al usuario en FirebaseAuth y crear su sesión en FirebaseDatabase.
class AuthenticationFirebase () {

    // Pantalla principal de Login. Es un Composable porque construye UI.
    // Recibe el navController para permitir navegación tras iniciar sesión.
    @Composable
    fun MenuLogin(navController: NavController) {

        // Estados manejados por Compose. Cuando cambian, la UI se recompone.
        var email by remember { mutableStateOf(value = "") }
        var password by remember { mutableStateOf(value = "") }

        // Controla si la contraseña se debe mostrar como texto o como puntos.
        var visibleInput by remember { mutableStateOf(value = false) }

        // Estados para mostrar errores bajo cada campo.
        var emailError by remember { mutableStateOf(value = false) }
        var passwordError by remember { mutableStateOf(value = false) }

        // Contexto de Android para mostrar Toasts
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título de la pantalla
            Text(
                text = "Iniciar Sesión",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para ingresar correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = email.isBlank()   // Validación: no permitir vacío
                },
                label = { Text(text = "Ingrese su correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailError // Activa borde rojo
            )

            // Texto de error si el correo está vacío
            if (emailError) {
                Text(
                    text = "El correo no puede quedar vacío",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para ingresar contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = password.isBlank()
                },
                label = { Text(text = "Ingrese su contraseña") },
                modifier = Modifier.fillMaxWidth(),

                // Mostrar contraseña o no
                visualTransformation =
                    if (visibleInput) VisualTransformation.None
                    else PasswordVisualTransformation(),

                // Botón dentro del campo para alternar visibilidad
                trailingIcon = {
                    TextButton(onClick = { visibleInput = !visibleInput }) {
                        Text(text = if (visibleInput) "Ocultar" else "Ver")
                    }
                },

                singleLine = true,
                isError = passwordError
            )

            // Texto de error si la contraseña está vacía
            if (passwordError) {
                Text(
                    text = "La contraseña no puede quedar vacía",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN: INGRESAR
            Button(
                onClick = {

                    // Actualizar flags de error
                    emailError = email.isBlank()
                    passwordError = password.isBlank()

                    // Validar que la contraseña no esté vacía
                    if (password.isNotEmpty()) {

                        // Validar formato del correo con Patterns.EMAIL_ADDRESS
                        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                            // Llamada a FirebaseAuth
                            loginFirebase(navController, context, email, password)

                        } else {
                            // Feedback al usuario cuando el email no es válido
                            Toast.makeText(context, "Correo inválido", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        // Si falta la contraseña → error
                        Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Ingresar")
            }
        }
    }

    // Crea el nodo "sesion_<userId>/dispositivo_data" en Firebase si no existe.
    private fun crearSesionUsuario(userId: String) {
        val database = Firebase.database
        val sesionRef = database.getReference("sesiones/sesion_$userId/dispositivo_data")

        // Listener que solo lee el nodo una vez.
        sesionRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                // Si la sesión no existe → crear datos por defecto
                if (!snapshot.exists()) {

                    // Estructura inicial almacenada en Firebase Realtime Database
                    val datosIniciales = mapOf(
                        "dispositivo_nombre" to "Arduino WEMOS D1",
                        "estado_agregado" to 0,
                        "corriente_detectada" to 1.0,
                        "alertas_dispositivo" to mapOf(
                            "estado" to false,
                            "rango_minimo" to 0.0,
                            "rango_maximo" to 0.0
                        ),
                        "datos_rele" to mapOf(
                            "estado_rele" to false,
                            "modo_uso" to 0
                        )
                    )

                    // Guardar datos en Firebase
                    sesionRef.setValue(datosIniciales)
                        .addOnSuccessListener {
                            println("Sesión creada para el usuario $userId")
                        }
                        .addOnFailureListener { error ->
                            println("Error al crear sesión: ${error.message}")
                        }

                } else {
                    // Ya existe → no hacer nada
                    println("Sesión existente, no se crea duplicado")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error al leer sesión: ${error.message}")
            }
        })
    }

    // Función que realiza login con FirebaseAuth usando correo y contraseña
    private fun loginFirebase(
        navController: NavController,
        context: android.content.Context,
        email: String,
        password: String
    ) {

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    // Obtener usuario actual
                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid ?: ""

                    // Crear estructura en Database (solo si no existía)
                    crearSesionUsuario(userId)

                    // Navegar a pantalla Inicio
                    navController.navigate("Inicio")

                    // Feedback visual
                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                } else {

                    // Obtener tipo de error
                    val exception = task.exception

                    val message = when (exception) {

                        is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
                            "Las credenciales ingresadas no están registradas"

                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                            "El correo o la contraseña son inválidos"

                        else ->
                            "Error al iniciar sesión: ${exception?.localizedMessage ?: "Intente nuevamente"}"
                    }

                    // Mostrar error mediante Toast
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Función para cerrar sesión usando FirebaseAuth
    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()

        val user = FirebaseAuth.getInstance().currentUser

        // Si el usuario ya no existe → éxito
        if (user == null) {
            println("Usuario deslogueado correctamente")
        } else {
            println("Usuario sigue logueado: ${user.email}")
        }
    }
}
