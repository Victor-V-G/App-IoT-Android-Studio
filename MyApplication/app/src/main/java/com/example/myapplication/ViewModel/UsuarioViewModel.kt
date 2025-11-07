package com.example.myapplication.ViewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class UsuarioViewModel : ViewModel() {
    val usuarios = mutableStateMapOf<String, String>()

    fun registrarUsuario(username: String, password: String) {
        usuarios[username] = password
    }
}