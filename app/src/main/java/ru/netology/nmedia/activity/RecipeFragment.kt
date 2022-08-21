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
import ru.netology.nmedia.activity.NewRecipeFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentRecipeBinding
import ru.netology.nmedia.dto.LongArg
import ru.netology.nmedia.dto.Utils
import ru.netology.nmedia.viewmodel.RecipeViewModel

class RecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRecipeBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)
        with(binding.recipeLayout) {
            viewModel.data.observe(viewLifecycleOwner) { recipes ->
                val recipe = recipes.find { it.id == arguments?.longArg }
                if (recipe != null) {
                    author.text = recipe.author
                    published.text = recipe.published
                    content.text = recipe.content
                    likeButton.isChecked = recipe.likedByMe
                    likeButton.text = Utils.counter(recipe.likes)
                    shareButton.text = Utils.counter(recipe.shares)
                    favoriteButton.isChecked = recipe.favoriteByMe

                    menu.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_recipe)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        findNavController().navigateUp()
                                        viewModel.deleteById(recipe.id)
                                        true
                                    }
                                    R.id.edit -> {
                                        viewModel.edit(recipe)
                                        findNavController().navigate(
                                            R.id.action_recipeFragment_to_newRecipeFragment,
                                            Bundle().apply
                                            {
                                                textArg = recipe.content
                                            })
                                        true
                                    }

                                    else -> false
                                }
                            }
                        }.show()
                    }

                    if (recipe.video != null) videoGroup.visibility = View.VISIBLE
                    else videoGroup.visibility = View.GONE

                    video.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.video))
                        startActivity(intent)
                    }

                    play.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.video))
                        startActivity(intent)
                    }


                    likeButton.setOnClickListener {
                        viewModel.likeById(recipe.id)
                    }

                    shareButton.setOnClickListener {
                        viewModel.shareById(recipe.id)
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, recipe.content)
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