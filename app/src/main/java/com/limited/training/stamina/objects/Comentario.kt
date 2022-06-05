package com.limited.training.stamina.objects

import com.google.firebase.database.PropertyName

class Comentario (
    @PropertyName("autor") val autor : String,
    @PropertyName("fecha") val fecha : Long,
    @PropertyName("mensaje") val mensaje : String,
) {
    constructor() : this("",0,"")
}
