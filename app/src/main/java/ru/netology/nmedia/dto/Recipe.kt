package ru.netology.nmedia.dto

data class Recipe(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var shares: Int = 0,
    var favByMe: Boolean = false,
    val video: String? = null
)