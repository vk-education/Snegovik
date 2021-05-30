package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.LiveData
import com.kinotech.kinotechappv1.ui.profile.SubsInfo

interface SubscribersRepository {
    fun getAll(): LiveData<List<SubsInfo>>
//    fun likedById(id: Int)
//    fun getSub(id: Int): SubsInfo?
}