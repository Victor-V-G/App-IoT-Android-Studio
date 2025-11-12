package com.example.componentestest.Componentes.Firebase

import com.google.android.gms.common.internal.Objects
// aqui se utiliza otra clase para crear un array de los datos configurados sobre las alertas
data class AlertasDispositivo(
    var estado: Boolean = false,
    var rango_minimo: Double = 0.0,
    var rango_maximo: Double = 0.0
)
data class DispositivoData(
    var dispositivo_nombre: String = "",
    var estado_agregado: Int = 0,
    var corriente_detectada: Double = 0.0,
    var alertas_dispositivo: AlertasDispositivo = AlertasDispositivo() //se agrega en forma de clase para crear un array con los datos definidos
) {
    constructor() : this ("", 0, 0.0, AlertasDispositivo())
}

