package com.kinotech.kinotechappv1.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Скоро здесь будет красивая лента"
    }
    val text: LiveData<String> = _text
}
