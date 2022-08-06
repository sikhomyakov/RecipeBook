package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Utils


internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ViewHolder(
    private val binding: PostBinding,
    private val listener: PostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeButton.text = Utils.counter(post.likes)
            shareButton.text = Utils.counter(post.shares)
            viewsButton.text = Utils.counter(post.views)
            likeButton.isChecked = post.likedByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemoveClicked(post)
                                true
                            }
                            R.id.edit -> {
                                listener.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            likeButton.setOnClickListener {
                listener.onLikeClicked(post)
            }
            shareButton.setOnClickListener {
                listener.onShareClicked(post)
            }
            viewsButton.setOnClickListener {
                listener.onViewClicked(post)
            }
        }
    }
}


class DiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Post, newItem: Post) =
        oldItem == newItem

}


