package ru.netology.recipesbook.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.recipesbook.adapter.RecipeAdapter
import ru.netology.recipesbook.databinding.FragmentRecipesFeedBinding
import ru.netology.recipesbook.viewmodel.RecipeViewModel


class RecipesFeedFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navigateToEditRecipe.observe(this) {
            val direction = RecipesFeedFragmentDirections.toEditRecipeFragment()
            findNavController().navigate(direction)
        }

        viewModel.navigateToNewRecipe.observe(this) {
            val direction = RecipesFeedFragmentDirections.toNewRecipeFragment()
            findNavController().navigate(direction)
        }

        viewModel.navigateToRecipe.observe(this) { recipe ->
            val direction = RecipesFeedFragmentDirections.toRecipeFragment(recipe.id)
            findNavController().navigate(direction)
        }

        viewModel.navigateToFavoriteRecipes.observe(this) {
            val direction = RecipesFeedFragmentDirections.toFaveRecipesFeedFragment()
            findNavController().navigate(direction)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRecipesFeedBinding.inflate(layoutInflater, container, false)
            .also { binding ->

                binding.fab.setOnClickListener {
                    viewModel.add()
                }

                val recipeAdapter = RecipeAdapter(viewModel)
                binding.recipesRecyclerView.adapter = recipeAdapter

                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    if (recipes.isNullOrEmpty()) {
                        binding.recipeFeedEmptyState.visibility = View.VISIBLE
                    } else {
                        binding.recipeFeedEmptyState.visibility = View.GONE
                    }

                    recipeAdapter.setData(recipes.toMutableList())
                }

            }.root
    }

}
