package com.example.myapplication.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Authentication.AuthenticationFirebase
import com.example.myapplication.Screens.PantallaAgregar
import com.example.myapplication.Screens.PantallaAlertas
import com.example.myapplication.Screens.PantallaDispositivo
import com.example.myapplication.Screens.PantallaEstablecerAlertas
import com.example.myapplication.Screens.PantallaInicio
import com.google.firebase.auth.FirebaseAuth

// Función principal de navegación de toda la app
// Aquí se define qué pantallas existen y cómo se navega entre ellas
@Composable
fun NavegacionApp() {

    // Obtiene el usuario actualmente autenticado en Firebase
    // Si es null → no hay sesión activa
    // Si NO es null → el usuario ya estaba loggeado
    val user = FirebaseAuth.getInstance().currentUser

    // Determina la pantalla inicial dependiendo de si hay sesión o no
    // Inicio → sesión activa
    // Login → no autenticado
    val startDestination = if (user != null) "Inicio" else "Login"

    // Crea un controlador de navegación, que se conserva en memoria mientras este Composable exista
    val navController: NavHostController = rememberNavController()

    // Instancia la clase donde está el Composable del login
    val authenticationClass = AuthenticationFirebase()

    // NavHost define TODA la estructura de navegación
    // startDestination indica qué pantalla se muestra al abrir la app
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // Ruta para la pantalla de Login
        // Se pasa el navController para poder navegar a otras pantallas después del inicio de sesión
        composable("Login") {
            authenticationClass.MenuLogin(navController)
        }

        // Ruta para la pantalla principal de la aplicación
        composable("Inicio") {
            PantallaInicio(navController)
        }

        // Ruta para la pantalla donde se agrega un dispositivo o una nueva configuración
        composable("Agregar") {
            PantallaAgregar(navController)
        }

        // Ruta para mostrar la información del dispositivo conectado
        composable("Dispositivo") {
            PantallaDispositivo(navController)
        }

        // Ruta para configurar los rangos de alertas mínimas y máximas
        composable("EstablecerAlertas") {
            PantallaEstablecerAlertas(navController)
        }

        // Ruta donde se muestran las alertas generadas o configuradas
        composable("Alertas") {
            PantallaAlertas(navController)
        }
    }
}
