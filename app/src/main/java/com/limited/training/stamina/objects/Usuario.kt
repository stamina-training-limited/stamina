package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

data class Usuario(
    @PropertyName("correo") val correo : String,
    @PropertyName("nombre") val nombre : String,
    @PropertyName("descripcion") val descripcion : String,
    @PropertyName("seguidores") val seguidores : List<String>,
    @PropertyName("seguidos") val seguidos : List<String>,
    @PropertyName("actividades") val actividades : List<String>,
    @PropertyName("rutas") val rutas : List<String>,
    @PropertyName("publicaciones") val publicaciones : List<String>,


){
    constructor() : this("", "", "", emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
}