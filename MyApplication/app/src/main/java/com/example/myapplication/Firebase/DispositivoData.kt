package com.example.componentestest.Componentes.Firebase

import com.google.android.gms.common.internal.Objects

// MODELOS UTILIZADOS PARA MAPEAR DATOS DE FIREBASE REALTIME DATABASE
//
// Firebase usa estructuras en formato JSON, pero en Kotlin es necesario
// convertir esos datos a clases (data class) para poder manipularlos fácilmente.
//
// Estas data classes representan exactamente la estructura:
// sesiones/
//    sesion_<userId>/
//        dispositivo_data/
//            dispositivo_nombre: "xxx"
//            estado_agregado: 0
//            corriente_detectada: 1.0
//            alertas_dispositivo/
//                estado: false
//                rango_minimo: 0.0
//                rango_maximo: 0.0
//            datos_rele/
//                estado_rele: false
//                modo_uso: 0



// Clase que representa la configuración de alertas del dispositivo
// Este objeto se guardará dentro del nodo "alertas_dispositivo"
data class AlertasDispositivo(
    var estado: Boolean = false,        // Indica si la alerta está activada o no
    var rango_minimo: Double = 0.0,     // Valor mínimo permitido antes de activar la alerta
    var rango_maximo: Double = 0.0      // Valor máximo permitido antes de activar la alerta
)



// Clase usada para representar la configuración del relé conectado al dispositivo
// Esta estructura se almacena dentro del nodo "datos_rele"
data class ReleData(
    var estado_rele: Boolean = false,  // Estado actual del relé (true = encendido)
    var modo_uso: Int = 0              // Modo de operación: 0 = manual, 1 = automático
)



// Clase principal que contiene TODA la información del dispositivo guardado en Firebase
// Esta corresponde al nodo "dispositivo_data" de cada usuario
data class DispositivoData(
    var dispositivo_nombre: String = "",                   // Nombre asignado al dispositivo
    var estado_agregado: Int = 0,                          // Indica si ya fue configurado (0 = no, 1 = sí)
    var corriente_detectada: Double = 0.0,                 // Corriente actual medida por el sensor

    // Objeto que contiene los datos de las alertas configuradas
    // Firebase interpreta este campo como un sub-objeto JSON automáticamente
    var alertas_dispositivo: AlertasDispositivo = AlertasDispositivo(),

    // Objeto que contiene los datos del relé
    // También es interpretado como un sub-objeto JSON
    var datos_rele: ReleData = ReleData()
) {

    // Constructor vacío requerido por Firebase
    // Firebase necesita este constructor sin parámetros para reconstruir
    // los objetos al leer información desde la base de datos
    constructor() : this(
        "",              // dispositivo_nombre
        0,               // estado_agregado
        0.0,             // corriente_detectada
        AlertasDispositivo(),
        ReleData()
    )
}
