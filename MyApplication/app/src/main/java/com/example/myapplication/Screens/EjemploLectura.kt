package com.example.componentestest.Componentes.Firebase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp


@Composable
fun EjemploLectura(){
    var sensor by rememberSaveable  { mutableStateOf<SensorData?>(null) }

    LeerFirebase("sensor_data", SensorData::class.java).apply {
        sensor = first ?: sensor
    }

    Column {
        Text( text = "Nombre: ${sensor?.device?:"Desconocido"}")
        Text( text = "ValorCrudo: ${sensor?.raw_value?:0}"  )
    }
}

