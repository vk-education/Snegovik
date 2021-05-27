package com.kinotech.kinotechappv1.ui.lists

sealed class AnyItemInAdapterList {
    class ButtonCreateList(
        val itemTitle: String,
        val imgList: String
    ) : AnyItemInAdapterList()

   class ButtonFavList(
        val itemTitle: String,
        val filmCount: String,
        val imgList: String
    ) : AnyItemInAdapterList()

    class ButtonShowList(
        val itemTitle: String,
        val filmCount: String,
        val imgList: String
    ) : AnyItemInAdapterList()
}
