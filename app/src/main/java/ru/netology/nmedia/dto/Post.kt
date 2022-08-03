package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 999,
    var likedByMe: Boolean = false,
    var shares: Int = 993,
    var sharedByMe: Boolean = false,
    var views: Int = 5,
    var viewedBy: Boolean = false,

)