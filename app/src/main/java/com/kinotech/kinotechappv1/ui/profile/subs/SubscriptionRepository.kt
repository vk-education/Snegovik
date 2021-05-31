package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

interface SubscriptionRepository {
    fun getAll(): LiveData<List<SubsInfo>>
//    fun getSub(id: Int): SubsInfo?
}