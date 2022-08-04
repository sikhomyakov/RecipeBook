package ru.netology.nmedia

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Utils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        val adapter = PostsAdapter(object : PostInteractionListener {
            override fun onLikeClicked(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShareClicked(post: Post) {
                viewModel.toShareById(post.id)
            }

            override fun onViewClicked(post: Post) {
                viewModel.toViewById(post.id)
            }

            override fun onRemoveClicked(post: Post) {
                viewModel.deleteById(post.id)
            }

            override fun onEditClicked(post: Post) {
                viewModel.edit(post)
            }
        })

        binding.posts.adapter = adapter
        viewModel.data.observe(this, { posts ->
            adapter.submitList(posts)
        })
        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }
            binding.postUndo.text = it.content
            binding.group.visibility = View.VISIBLE
            with(binding.inputTextArea) {
                requestFocus()
                setText(it.content)
            }
        }

        binding.confirmationButton.setOnClickListener {
            with(binding.inputTextArea) {
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(
                        this@MainActivity,
                        context.getString(R.string.error_empty_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.editContent(text.toString())
                viewModel.addPost()

                setText("")
                binding.group.visibility = View.GONE
                clearFocus()
                Utils.hideKeyboard(this)
            }
        }

        binding.cancelEditingButton.setOnClickListener {
            with(binding.inputTextArea) {
                viewModel.cancelEdit()
                setText("")
                binding.group.visibility = View.GONE
                clearFocus()
                Utils.hideKeyboard(this)
            }
        }
    }
}

