//package com.kinotech.kinotechappv1.ui.feed
//
//import androidx.lifecycle.ViewModel
//import com.kinotech.kinotechappv1.ui.feed.repository.NewListPostRepository
//import com.kinotech.kinotechappv1.ui.feed.repository.NewListPostRepositoryImpl
//
//class FeedViewModel : ViewModel() {
//
//    private val repository: NewListPostRepository = NewListPostRepositoryImpl()
//    val newListPosts = repository.getAll()
//    fun likedById(id: Int) = repository.likedById(id)
//}
