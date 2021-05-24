package com.kinotech.kinotechappv1.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileSharedViewModel : ViewModel() {
    private val photo = MutableLiveData<Uri>()

    fun putPhoto(uri: Uri) {
        photo.value = uri
    }

    fun getPhoto(): LiveData<Uri> {
        return photo
    }
}