package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.ViewModel

class SubscribersViewModel : ViewModel() {
    private val repository: SubscribersRepository = SubscribersRepositoryImpl()
    val subscribers = repository.getAll()
    fun likedById(id: Int) = repository.likedById(id)
//    fun getSub(id: Int) = repository.getSub(id)
}