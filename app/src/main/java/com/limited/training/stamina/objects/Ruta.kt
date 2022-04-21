package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

data class Ruta(
    @PropertyName("nombre") val nombre: String,
    @PropertyName("distancia") val distancia: Double,
    @PropertyName("tipo") val tipo: String,
) {
    constructor() : this("", 0.0, "")
}