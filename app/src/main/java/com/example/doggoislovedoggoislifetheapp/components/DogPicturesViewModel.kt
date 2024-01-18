package com.example.doggoislovedoggoislifetheapp.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DogPicturesViewModel : ViewModel() {
    private val _likedPictures = mutableMapOf<String, String>()
    val likedPictures: Map<String, String> get() = _likedPictures

    fun addLikedPicture(url: String, breed: String) {
        viewModelScope.launch {
            _likedPictures[url] = breed
        }
    }

    fun removeLikedPicture(url: String) {
        viewModelScope.launch{
            _likedPictures.remove(url)
        }
    }
}