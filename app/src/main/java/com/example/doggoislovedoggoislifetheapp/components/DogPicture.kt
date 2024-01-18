package com.example.doggoislovedoggoislifetheapp.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class DogPicture(
    val imageUrl: String,
    val isLiked: MutableState<Boolean> = mutableStateOf(false)
)


