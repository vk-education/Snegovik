package com.kinotech.kinotechappv1.ui.feed.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.feed.PostNewList

class NewListPostRepositoryImpl : NewListPostRepository {

    private var postsNL = listOf(
        PostNewList(
            id = 2,
            author = "Мария Соколова",
            postingDate = "5 минут назад",
            profilePic = R.drawable.ic_profile_circle_24,
            actionDoneText = "Добавлен новый список \"Хорошо похохотать\"",
            likesCount = 3,
            viewsCount = 5,
            film1 = R.drawable.film1,
            film2 = R.drawable.film2,
            film3 = R.drawable.film3
        ),

        PostNewList(
            id = 1,
            author = "Владимир Куликов",
            postingDate = "1 час назад",
            profilePic = R.drawable.ic_profile_circle_24,
            actionDoneText = "Добавлен новый список \"Грустненькое\"",
            likesCount = 10,
            viewsCount = 50,
            film1 = R.drawable.film3,
            film2 = R.drawable.film1,
            film3 = R.drawable.film2
        )
    )

    private val data = MutableLiveData(postsNL)

    override fun getAll(): LiveData<List<PostNewList>> = data

    override fun likedById(id: Int) {
        postsNL = postsNL.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            )
        }
        data.value = postsNL
    }
}
