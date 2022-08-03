package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Utils

interface OnItemClickListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onView(post: Post) {}
}
internal class PostsAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: PostBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(post: Post) = with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = Utils.counter(post.likes)
            shareCount.text = Utils.counter(post.shares)
            viewCount.text = Utils.counter(post.views)
            like.setImageResource(
                if (post.likedByMe) {
                    R.drawable.ic_liked_24
                } else {
                    R.drawable.ic_like_24
                }
            )
            like.setOnClickListener {
                onItemClickListener.onLike(post)
            }
            share.setOnClickListener {
                onItemClickListener.onShare(post)
            }
            view.setOnClickListener {
                onItemClickListener.onView(post)
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }



}