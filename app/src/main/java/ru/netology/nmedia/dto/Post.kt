package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int,
    val likedByMe: Boolean,
    var shares: Int,
    val sharedByMe: Boolean,
    val views: Int,
    val viewedBy: Boolean
)