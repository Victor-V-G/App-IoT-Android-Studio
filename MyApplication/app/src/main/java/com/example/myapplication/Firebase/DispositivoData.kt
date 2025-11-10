package com.example.componentestest.Componentes.Firebase

data class DispositivoData(
    var dispositivo_nombre: String = "",
    var estado_agregado: Int = 0
) {
    constructor() : this ("", 0)
}