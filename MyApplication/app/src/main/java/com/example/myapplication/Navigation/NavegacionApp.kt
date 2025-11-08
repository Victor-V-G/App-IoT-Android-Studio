package com.example.myapplication.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import  androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Authentication.AuthenticationFirebase
import com.example.myapplication.Screens.PantallaInicio

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()
    val authenticationClass = AuthenticationFirebase()
    NavHost(
        navController = navController,
        startDestination = "Login"
    ) {
        composable("Login"){
            authenticationClass.MenuLogin(navController)
        }
        composable("Inicio"){
            PantallaInicio(navController)
        }
    }
}