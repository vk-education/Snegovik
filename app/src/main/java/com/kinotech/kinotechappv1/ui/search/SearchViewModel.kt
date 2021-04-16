package com.kinotech.kinotechappv1.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ищите любимые фильмы и сериалы с помощью поиска наверху!"
    }
    val text: LiveData<String> = _text
}
