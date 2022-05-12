package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Icons
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
    fun counter(icon: Icons) = data.value?.let {
        when (icon) {
            Icons.LIKES -> repository.counter(it.likes)
            Icons.SHARES -> repository.counter(it.shares)
            Icons.VIEWS -> repository.counter(it.views)
        }
    }
}