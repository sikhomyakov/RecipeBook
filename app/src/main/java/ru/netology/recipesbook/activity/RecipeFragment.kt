package ru.netology.recipesbook.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import ru.netology.recipesbook.R
import ru.netology.recipesbook.adapter.StepsAdapter
import ru.netology.recipesbook.databinding.FragmentRecipeBinding
import ru.netology.recipesbook.dto.Categories
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.Utils
import ru.netology.recipesbook.viewmodel.RecipeViewModel


class RecipeFragment : Fragment() {
    private val args by navArgs<RecipeFragmentArgs>()

    private val viewModel by activityViewModels<RecipeViewModel>()

    private lateinit var recipe: Recipe

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipe_options, menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                viewModel.onEditClicked(args.recipeId)
                viewModel.navigateToEditRecipe.observe(this) {
                    val direction = RecipesFeedFragmentDirections.toEditRecipeFragment()
                    findNavController().navigate(direction)
                }
                true
            }
            R.id.remove -> {
                findNavController().popBackStack()
                viewModel.onRemoveClicked(args.recipeId)
                true
            }
            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRecipeBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {

                setHasOptionsMenu(true)



                viewModel.data.value?.let { listOfRecipes ->
                    recipe = listOfRecipes.first { recipe -> recipe.id == args.recipeId }
                    render(recipe)
                }

                viewModel.data.observe(viewLifecycleOwner) { listOfRecipes ->
                    if (!listOfRecipes.any { recipe -> recipe.id == args.recipeId }) {
                        return@observe
                    }
                    if (listOfRecipes.isNullOrEmpty()) {
                        return@observe

                    }
                    recipe = listOfRecipes.first { recipe -> recipe.id == args.recipeId }
                    render(recipe)


                }

                setTag(recipeChipGroup.context, recipe.categories, binding)

                favoriteRecipeMaterialButton.setOnClickListener {
                    viewModel.onFavoriteClicked(recipe)
                }
                likeRecipeMaterialButton.setOnClickListener {
                    viewModel.onLikeClicked(recipe)
                }
            }
        }.root
    }

    private fun FragmentRecipeBinding.render(recipe: Recipe) {
        recipeNameTextView.text = recipe.title
        recipeImageView.setImageURI(Uri.parse(recipe.recipeImg))
        recipeFragmentAuthor.text = recipe.author
        recipeFragmentPublished.text = recipe.published
        recipeContentWordTextView.text = recipe.content
        likeRecipeMaterialButton.text = Utils.counter(recipe.likes)
        likeRecipeMaterialButton.isChecked = recipe.likedByMe
        favoriteRecipeMaterialButton.isChecked = recipe.favoriteByMe

        val stepsAdapter = StepsAdapter(recipe, CALLER_RECIPE, viewModel)
        recipeStepsList.adapter = stepsAdapter
        stepsAdapter.submitList(recipe.steps.keys.toList())
    }

    private fun setTag(
        context: Context,
        categories: MutableSet<Categories>,
        binding: FragmentRecipeBinding
    ) {
        val chipGroup = binding.recipeChipGroup
        categories.forEach { category ->
            val categoryName = category.categoryName
            val chip = Chip(context)
            chip.text = categoryName
            chipGroup.addView(chip)
        }
    }

    companion object {
        const val CALLER_RECIPE = "Caller: recipe"
    }
}
