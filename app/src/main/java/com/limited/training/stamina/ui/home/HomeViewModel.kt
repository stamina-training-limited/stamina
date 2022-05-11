package com.limited.training.stamina.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limited.training.stamina.objects.Publication
import com.limited.training.stamina.objects.Ruta

class HomeViewModel : ViewModel() {

    val selected = MutableLiveData<Publication>()

    fun select(item: Publication) {
        selected.value = item
    }

}