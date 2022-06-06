package com.limited.training.stamina.ui.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limited.training.stamina.Util.SelectViewModel
import com.limited.training.stamina.objects.Usuario

class UserViewModel : SelectViewModel() {

    val selected_u = MutableLiveData<Usuario>()

    fun select(item: Usuario){
        selected_u.value = item
    }
}