package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.adapter.*
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(object : OnItemClickListener {
            override fun onLikeClicked(post: Post) {
                viewModel.onLikeClicked(post.id)
            }

            override fun onShareClicked(post: Post) {
                viewModel.onShareClicked(post.id)
            }

            override fun onViewClicked(post: Post) {
                viewModel.onViewClicked(post.id)
            }

            override fun onDeleteClicked(post: Post) {
                viewModel.onDeleteClicked(post.id)
            }
        })

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}

