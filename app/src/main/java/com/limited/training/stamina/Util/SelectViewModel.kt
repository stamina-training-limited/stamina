package com.limited.training.stamina.Util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.limited.training.stamina.objects.Publication

open class SelectViewModel : ViewModel() {

    val selected = MutableLiveData<Pair<Publication, Int>>()

    fun select(item: Pair<Publication, Int>) {
        selected.value = item
    }

}