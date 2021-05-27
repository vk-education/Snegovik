package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData

interface SubscriptionRepository {
    fun getAll(): LiveData<List<SubsInfo>>
//    fun getSub(id: Int): SubsInfo?
}