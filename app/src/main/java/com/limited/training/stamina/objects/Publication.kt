package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

class Publication (
        @PropertyName("titulo") val titulo: String,
        @PropertyName("nombre") val nombre: String,
        @PropertyName("hora") val hora: String,
        @PropertyName("lugar") val lugar: String,
        @PropertyName("distancia") val distancia: Double,
        @PropertyName("ritmo") val ritmo: Double,
        @PropertyName("tiempo") val tiempo: Double
) {
    constructor() : this("","", "", "", 0.0, 0.0, 0.0)
}
