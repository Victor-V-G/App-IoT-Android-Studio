package com.example.myapplication.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.componentestest.Componentes.Firebase.EjemploLectura

@Composable
fun PantallaDetalle(nombre: String, navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Hola, $nombre")

        Button(
            onClick = {
                navController.navigate("datos")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Mostrar datos")
        }

    }

}



