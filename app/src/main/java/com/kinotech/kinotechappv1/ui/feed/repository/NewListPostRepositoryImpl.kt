package com.kinotech.kinotechappv1.ui.feed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.feed.PostNewList

class NewListPostRepositoryImpl : NewListPostRepository {

    private var postsNL = arrayListOf<PostNewList>()

    private val data = MutableLiveData(postsNL)

    override fun getAll(): LiveData<ArrayList<PostNewList>> = data

    override fun likedById(id: Int) {
        postsNL = postsNL.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            )
        } as ArrayList<PostNewList>
        data.value = postsNL
    }
}
