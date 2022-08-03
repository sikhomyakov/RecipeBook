package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    fun like(id: Long)
    fun share(id: Long)
    fun delete(id: Long)
    fun view(id: Long)
    fun edit(id: Long)
}