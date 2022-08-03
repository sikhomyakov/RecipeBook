package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface PostInteractionListener {
    fun onLikeClicked(id: Long)
    fun onShareClicked(id: Long)
    fun onViewClicked(id: Long)
    fun onDeleteClicked(id: Long)
    fun onEditClicked(id: Long)
}