package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinotech.kinotechappv1.R

class SubscribersRepositoryImpl : SubscribersRepository {

    private var subs = listOf(
        SubsInfo(
            id = 1,
            name = "Маша Соколова",
            subscribed = false,
            profilePic = R.drawable.ic_profile_circle_24
        ),
        SubsInfo(
            id = 2,
            name = "Ваня Лебедев",
            subscribed = true,
            profilePic = R.drawable.ic_profile_circle_24
        )

    )

    private val data = MutableLiveData(subs)

    override fun getAll(): LiveData<List<SubsInfo>> = data

    override fun likedById(id: Int) {
        subs = subs.map {
            if (it.id != id) it else it.copy(subscribed = !it.subscribed)
        }
        data.value = subs
    }

//    override fun getSub(id: Int): SubsInfo? {
//        subs.map {
//            return if (it.id == id) it else null
//        }
//        return subs
//    }
}