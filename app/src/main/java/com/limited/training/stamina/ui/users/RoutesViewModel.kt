package com.limited.training.stamina.ui.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limited.training.stamina.objects.Ruta

class RoutesViewModel : ViewModel() {

    val selected = MutableLiveData<Ruta>()

    fun select(item: Ruta) {
        selected.value = item
    }

}