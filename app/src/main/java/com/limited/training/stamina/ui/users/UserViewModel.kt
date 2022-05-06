package com.limited.training.stamina.ui.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limited.training.stamina.objects.Usuario

class UserViewModel : ViewModel() {

    val selected = MutableLiveData<Usuario>()

    fun select(item: Usuario){
        selected.value = item
    }
}