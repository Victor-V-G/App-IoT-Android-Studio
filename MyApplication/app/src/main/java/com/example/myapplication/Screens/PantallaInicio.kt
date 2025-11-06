package com.example.myapplication.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun PantallaInicio(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pantalla de inicio",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                navController.navigate("detalle/Pablo")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Ir a detalle")
        }

    }
}