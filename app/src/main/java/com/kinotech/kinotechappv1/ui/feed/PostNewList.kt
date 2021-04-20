package com.kinotech.kinotechappv1.ui.feed

data class PostNewList(
    val id: Int,
    val author: String,
    val postingDate: String,
    val profilePic: Int,
    val actionDoneText: String,
    val likesCount: Int,
    val viewsCount: Int,
    val likedByMe: Boolean = false,
    val addedByMe: Boolean = false,
    val film1: Int, // Потом все нужно будет заменить на список
    val film2: Int,
    val film3: Int
)