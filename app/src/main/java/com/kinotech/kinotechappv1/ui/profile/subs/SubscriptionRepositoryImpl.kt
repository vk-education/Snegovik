package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinotech.kinotechappv1.R

class SubscriptionRepositoryImpl : SubscriptionRepository {

    private var subs = listOf(
        SubsInfo(
            id = 1,
            name = "Маша Соколова",
            subscribed = true,
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
}