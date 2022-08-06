package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()

        val postLauncher = registerForActivityResult(PostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }
        val adapter = PostsAdapter(object : PostInteractionListener {
            override fun onLikeClicked(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShareClicked(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }

            override fun onViewClicked(post: Post) {
                viewModel.toViewById(post.id)
            }

            override fun onRemoveClicked(post: Post) {
                viewModel.deleteById(post.id)
            }

            override fun onEditClicked(post: Post) {
                viewModel.edit(post)
                postLauncher.launch(post.content)
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            postLauncher.launch(null)
        }

//        val activityLauncher = registerForActivityResult(
//            NewPostActivity.ResultContract
//        ) { postContent: String? ->
//            postContent?.let(viewModel::onCreateNewPost)
//        }
//        activityLauncher.launch(Unit)

    }
}

