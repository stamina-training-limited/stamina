package com.limited.training.stamina.ui.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limited.training.stamina.objects.Coordenada

class RecordViewModel : ViewModel() {

    val coordenadasList = MutableLiveData<List<Coordenada>>()
    fun select(coordList: List<Coordenada>){
        coordenadasList.value = coordList
    }
}