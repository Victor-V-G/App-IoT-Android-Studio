package com.example.myapplication.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import  androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Login.Login
import com.example.myapplication.Registro.RegistroUsuario
import com.example.myapplication.ViewModel.UsuarioViewModel

@Composable
fun NavegacionApp() {
    val navController: NavHostController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "Login"
    ) {
        composable("Login"){
            Login(navController, usuarioViewModel)
        }
        composable("Registro"){
            RegistroUsuario(navController,usuarioViewModel)
        }
    }
}