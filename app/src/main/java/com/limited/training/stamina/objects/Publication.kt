package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

class Publication (
        @PropertyName("ref") val ref : String,
        @PropertyName("comentario") val comentario : List<Comentario>,
        @PropertyName("usuario") val usuario: String,
        @PropertyName("titulo") val titulo : String,
        @PropertyName("nombre") val nombre : String,
        @PropertyName("hora") val hora : String,
        @PropertyName("lugar") val lugar : String,
        @PropertyName("distancia") val distancia : Double,
        @PropertyName("ritmo") val ritmo : Double,
        @PropertyName("tiempo") val tiempo : Long,
        @PropertyName("megustas") val megustas : List<String>
) {
    constructor() : this("", emptyList(),"","","", "", "", 0.0, 0.0, 0, emptyList())
}
