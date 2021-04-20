package com.kinotech.kinotechappv1.ui.feed.repository

import androidx.lifecycle.LiveData
import com.kinotech.kinotechappv1.ui.feed.PostNewList

interface NewListPostRepository {
    fun getAll(): LiveData<List<PostNewList>>
    fun likedById(id: Int)
}