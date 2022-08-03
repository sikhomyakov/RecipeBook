package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel(), PostInteractionListener{

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data by repository::data
    override fun onLikeClicked(id: Long) = repository.like(id)
    override fun onDeleteClicked(id: Long) = repository.delete(id)
    override fun onShareClicked(id: Long) = repository.share(id)
    override fun onViewClicked(id: Long) = repository.view(id)
    override fun onEditClicked(id: Long) = repository.edit(id)

}