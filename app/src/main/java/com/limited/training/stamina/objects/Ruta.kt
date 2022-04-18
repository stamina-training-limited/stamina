package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

data class Ruta(
    @PropertyName("Likes") val likes: Int,
    @PropertyName("Distancia") val distancia: Double,
    @PropertyName("Nombre") val nombre: String,
) {
    constructor() : this(0, 0.0, "")
}