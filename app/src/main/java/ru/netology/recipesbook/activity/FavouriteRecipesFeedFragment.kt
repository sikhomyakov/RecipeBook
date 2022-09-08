package ru.netology.recipesbook.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.recipesbook.adapter.RecipeAdapter
import ru.netology.recipesbook.databinding.FragmentRecipesFeedFavoritesBinding
import ru.netology.recipesbook.viewmodel.RecipeViewModel


class FavouriteRecipesFeedFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToEditRecipe.observe(this) {
            val direction = FavouriteRecipesFeedFragmentDirections.toEditRecipeFragment()
            findNavController().navigate(direction)
        }

        viewModel.navigateToNewRecipe.observe(this) {
            val direction = FavouriteRecipesFeedFragmentDirections.toNewRecipeFragment()
            findNavController().navigate(direction)
        }

        viewModel.navigateToRecipe.observe(this) { recipe ->
            val direction = FavouriteRecipesFeedFragmentDirections.toRecipeFragment(recipe.id)
            findNavController().navigate(direction)
        }

        viewModel.navigateToAllRecipes.observe(this) {
            val direction = FavouriteRecipesFeedFragmentDirections.toRecipesFeedFragment()
            findNavController().navigate(direction)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRecipesFeedFavoritesBinding.inflate(layoutInflater, container, false)
            .also { binding ->

                val recipeAdapter = RecipeAdapter(viewModel)
                binding.favoriteRecipesRecyclerView.adapter = recipeAdapter

                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    val favouriteRecipes = recipes.filter { recipe -> recipe.favoriteByMe }
                    if (favouriteRecipes.isEmpty()) {
                        binding.emptyStateFavoritesFeedGroup.visibility = View.VISIBLE
                    } else {
                        binding.emptyStateFavoritesFeedGroup.visibility = View.GONE
                    }

                    recipeAdapter.submitList(favouriteRecipes)
                }

            }.root
    }
}