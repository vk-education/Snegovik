package com.kinotech.kinotechappv1.ui.profile.subs

import androidx.lifecycle.ViewModel

class SubscriptionViewModel : ViewModel() {
    private val repository: SubscriptionRepository = SubscriptionRepositoryImpl()
    val subscription = repository.getAll()
}