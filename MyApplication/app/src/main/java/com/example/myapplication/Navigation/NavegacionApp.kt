package com.example.myapplication.Navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import  androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.componentestest.Componentes.Firebase.EjemploLectura
import com.example.myapplication.Screens.PantallaDetalle
import com.example.myapplication.Screens.PantallaInicio

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Inicio"
    ) {
        composable("Inicio") {
            PantallaInicio(navController)
        }
        composable("detalle/{nombre}") { backStackEntry ->
            val nombre = backStackEntry
                .arguments
                ?.getString("nombre") ?: "Invitado"

            PantallaDetalle(nombre, navController)
        }
        composable("datos") { backStackEntry ->
            EjemploLectura()
        }
    }
}