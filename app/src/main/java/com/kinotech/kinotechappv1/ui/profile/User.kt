package com.kinotech.kinotechappv1.ui.profile

class User {
    private var fullName: String = ""
    private var photo: String = ""
    private var uid: String = ""

    constructor()

    constructor(fullName: String, photo: String){
        this.fullName = fullName
        this.photo = photo
        this.uid = uid

    }

    fun getFullName(): String{
        return fullName
    }
    fun setFullName(fullName: String){
        this.fullName = fullName
    }
    fun getPhoto(): String{
        return photo
    }
    fun setPhoto(photo: String){
        this.photo = photo
    }
    fun getUid(): String{
        return uid
    }
    fun setUid(uid: String){
        this.uid = uid
    }
}