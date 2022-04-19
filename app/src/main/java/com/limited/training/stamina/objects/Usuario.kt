package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

data class Usuario(
    @PropertyName("Correo") val correo : String,
    @PropertyName("Nombre") val nombre : String,
    @PropertyName("Descripcion") val descripcion : String,
    @PropertyName("Seguidores") val seguidores : List<String>,
    @PropertyName("Seguidos") val seguidos : List<String>,
    @PropertyName("Actividades") val actividades : List<String>,
    @PropertyName("Rutas") val rutas : List<String>,
    @PropertyName("Publicaciones") val publicaciones : List<String>,


){
    constructor() : this("", "", "", emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
}