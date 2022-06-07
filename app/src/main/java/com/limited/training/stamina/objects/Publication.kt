package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

class Publication (
        @PropertyName("ref") var ref : String,
        @PropertyName("comentarios") var comentarios : List<Comentario>,
        @PropertyName("usuario") var usuario: String,
        @PropertyName("titulo") var titulo : String,
        @PropertyName("nombre") var nombre : String,
        @PropertyName("hora") var hora : String,
        @PropertyName("lugar") var lugar : String,
        @PropertyName("distancia") var distancia : Double,
        @PropertyName("ritmo") var ritmo : Double,
        @PropertyName("tiempo") var tiempo : Long,
        @PropertyName("megustas") var megustas : List<String>
) {
    constructor() : this("", emptyList(),"","","", "", "", 0.0, 0.0, 0, emptyList())
}
