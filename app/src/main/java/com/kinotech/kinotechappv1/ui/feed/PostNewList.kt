package com.kinotech.kinotechappv1.ui.feed

data class PostNewList(
    val fullName: String = "",
    val photo: String = "",
    val actionDoneText: String = "",
    val films: List<String> = arrayListOf(),
    val uid: String = ""
)
