package com.example.myapplication.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import  androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Authentication.AuthenticationFirebase
import com.example.myapplication.Screens.PantallaAgregar
import com.example.myapplication.Screens.PantallaInicio
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavegacionApp() {

    val user = FirebaseAuth.getInstance().currentUser
    val startDestination = if (user != null) "Inicio" else "Login"

    val navController: NavHostController = rememberNavController()

    val authenticationClass = AuthenticationFirebase()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("Login"){
            authenticationClass.MenuLogin(navController)
        }
        composable("Inicio"){
            PantallaInicio(navController)
        }
        composable("Agregar"){
            PantallaAgregar(navController)
        }
    }
}