package ru.netology.recepiebook.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.recepiebook.R
import ru.netology.recepiebook.activity.NewRecipeFragment.Companion.textArg
import ru.netology.recepiebook.activity.RecipeFragment.Companion.longArg
import ru.netology.recepiebook.adapter.RecipeAdapter
import ru.netology.recepiebook.adapter.RecipeInteractionListener
import ru.netology.recepiebook.databinding.FragmentFeedBinding
import ru.netology.recepiebook.dto.Recipe
import ru.netology.recepiebook.viewmodel.RecipeViewModel


class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel by viewModels<RecipeViewModel>(ownerProducer = ::requireParentFragment)


        val adapter = RecipeAdapter(object : RecipeInteractionListener {
            override fun onVideoClicked(recipe: Recipe) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.video))
                startActivity(intent)
            }

            override fun onRecipeClicked(recipe: Recipe) {
                findNavController().navigate(R.id.action_feedFragment_to_recipeFragment,
                    Bundle().apply {
                        longArg = recipe.id
                    })
            }

            override fun onLikeClicked(recipe: Recipe) {
                viewModel.likeById(recipe.id)
            }

            override fun onShareClicked(recipe: Recipe) {
                viewModel.shareById(recipe.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, recipe.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_recipe))
                startActivity(shareIntent)
            }


            override fun onRemoveClicked(recipe: Recipe) {
                viewModel.deleteById(recipe.id)
            }

            override fun onFavoriteClicked(recipe: Recipe) {
                    viewModel.favoriteById(recipe.id)
            }

            override fun onEditClicked(recipe: Recipe) {
                viewModel.edit(recipe)
                findNavController().navigate(R.id.action_feedFragment_to_newRecipeFragment, Bundle()
                    .apply {
                        textArg = recipe.content
                    })
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { recipes ->
            adapter.submitList(recipes)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newRecipeFragment)
        }
        return binding.root
    }
}

