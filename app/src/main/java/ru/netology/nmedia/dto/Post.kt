package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likedByMe: Boolean = false,
    var likes: Int = 999,
    var shares: Int = 993,
    var views: Int = 5,
    val video: String? = null
)