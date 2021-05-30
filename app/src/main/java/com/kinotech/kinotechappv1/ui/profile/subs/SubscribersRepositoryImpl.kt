package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscribersRepositoryImpl : SubscribersRepository {

    private var subs = listOf(
        SubsInfo(
            uid = "1",
            fullName = "Маша Соколова",
            email = "rb",
            profilePic = R.drawable.ic_profile_circle_24
        ),
        SubsInfo(
            uid = "2",
            fullName = "Ваня Лебедев",
            email = "hg",
            profilePic = R.drawable.ic_profile_circle_24
        )

    )

    private val data = MutableLiveData(subs)

    override fun getAll(): LiveData<List<SubsInfo>> = data

//    override fun likedById(id: Int) {
//        subs = subs.map {
//            if (it.id != id) it else it.copy(subscribed = !it.subscribed)
//        }
//        data.value = subs
//    }

//    override fun getSub(id: Int): SubsInfo? {
//        subs.map {
//            return if (it.id == id) it else null
//        }
//        return subs
//    }
}