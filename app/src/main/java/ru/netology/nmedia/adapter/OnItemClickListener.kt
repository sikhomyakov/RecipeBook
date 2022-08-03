package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface OnItemClickListener {
    fun onLikeClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onViewClicked(post: Post)
    fun onDeleteClicked(post: Post)
}