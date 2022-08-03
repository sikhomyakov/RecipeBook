package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data
    fun onLikeClicked(id: Long) = repository.like(id)
    fun onDeleteClicked(id: Long) = repository.delete(id)
    fun onShareClicked(id: Long) = repository.share(id)
    fun onViewClicked(id: Long) = repository.view(id)

}