package com.kinotech.kinotechappv1.ui.feed.repository

import androidx.lifecycle.LiveData
import com.kinotech.kinotechappv1.ui.feed.PostNewList

interface NewListPostRepository {
    fun getAll(): LiveData<ArrayList<PostNewList>>
    fun likedById(id: Int)
}
