package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.LongArg
import ru.netology.nmedia.dto.Utils
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        with(binding.postLayout) {
            viewModel.data.observe(viewLifecycleOwner) { posts ->
                val post = posts.find { it.id == arguments?.longArg }
                if (post != null) {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content
                    likeButton.isChecked = post.likedByMe
                    likeButton.text = Utils.counter(post.likes)
                    shareButton.text = Utils.counter(post.shares)
                    viewsButton.text = Utils.counter(post.views)

                    menu.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        findNavController().navigate(R.id.action_postFragment_to_feedFragment)
                                        viewModel.deleteById(post.id)
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.edit(post)
                                        findNavController().navigate(
                                            R.id.action_postFragment_to_newPostFragment,
                                            Bundle().apply
                                            {
                                                textArg = post.content
                                            })
                                        true
                                    }

                                    else -> false
                                }
                            }
                        }.show()
                    }

                    if (post.video != null) videoGroup.visibility = View.VISIBLE
                    else videoGroup.visibility = View.GONE

                    video.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                        startActivity(intent)
                    }

                    play.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                        startActivity(intent)
                    }


                    likeButton.setOnClickListener {
                        viewModel.likeById(post.id)
                    }

                    shareButton.setOnClickListener {
                        viewModel.shareById(post.id)
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, post.content)
                            type = "text/plain"
                        }
                        val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_share_post))
                        startActivity(shareIntent)
                    }
                }
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.longArg: Long by LongArg

    }
}