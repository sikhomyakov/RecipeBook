package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun toShareById(id: Long)
    fun toViewById(id: Long)
    fun deleteById(id: Long)
    fun addPost(post: Post)
}