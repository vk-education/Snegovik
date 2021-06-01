package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kinotech.kinotechappv1.R
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

class SubscriptionRepositoryImpl : SubscriptionRepository {

    private var subs = listOf(
        SubsInfo(
            uid = "1",
            fullName = "Маша Соколова",
            email = "gf",
            profilePic = R.drawable.ic_profile_circle_24
        ),
        SubsInfo(
            uid = "dv",
            fullName = "Ваня Лебедев",
            email = "ds",
            profilePic = R.drawable.ic_profile_circle_24
        )
    )

    private val data = MutableLiveData(subs)

    override fun getAll(): LiveData<List<SubsInfo>> = data
}