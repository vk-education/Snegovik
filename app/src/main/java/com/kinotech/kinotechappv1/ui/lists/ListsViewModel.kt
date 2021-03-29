package com.kinotech.kinotechappv1.ui.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Тут будут списки"
    }
    val text: LiveData<String> = _text
}
