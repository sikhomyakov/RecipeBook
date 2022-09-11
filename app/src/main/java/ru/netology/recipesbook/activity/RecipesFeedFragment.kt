package ru.netology.recipesbook.activity


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import ru.netology.recipesbook.R
import ru.netology.recipesbook.adapter.RecipeAdapter
import ru.netology.recipesbook.databinding.FragmentRecipesFeedBinding
import ru.netology.recipesbook.viewmodel.RecipeViewModel


class RecipesFeedFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()
    private val openSearch = MutableLiveData(false)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search_button -> {
                if (openSearch.value != null) {
                    openSearch.value = !openSearch.value!!
                }
                true
            }

            R.id.menu_filter_button -> {
                val direction = RecipesFeedFragmentDirections.toFiltersFragment()
                findNavController().navigate(direction)
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
        return FragmentRecipesFeedBinding.inflate(layoutInflater, container, false)
            .also { binding ->

                setHasOptionsMenu(true)

                binding.fab.setOnClickListener {
                    viewModel.add()
                }

                val recipeAdapter = RecipeAdapter(viewModel)
                binding.recipesRecyclerView.adapter = recipeAdapter

                viewModel.data.observe(viewLifecycleOwner) { recipes ->
                    if (recipes.isNullOrEmpty()) {
                        binding.emptyStateFeedGroup.visibility = View.VISIBLE
                    } else {
                        binding.emptyStateFeedGroup.visibility = View.GONE
                    }

                    recipeAdapter.setData(recipes.toMutableList())
                }

                viewModel.filterRecipes.observe(viewLifecycleOwner) {
                    if (viewModel.filteredRecipes.value.isNullOrEmpty()) {
                        binding.emptyStateFeedGroup.visibility = View.VISIBLE
                    } else {
                        binding.emptyStateFeedGroup.visibility = View.GONE
                    }

                    recipeAdapter.setData(viewModel.filteredRecipes.value?.toMutableList())
                }

                openSearch.observe(viewLifecycleOwner) { openSearch ->
                    if (openSearch) {
                        binding.recipesSearchView.visibility = View.VISIBLE
                        binding.fab.visibility = View.GONE
                    } else {
                        binding.recipesSearchView.visibility = View.GONE
                        binding.fab.visibility = View.VISIBLE
                    }

                    binding.recipesSearchView.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String): Boolean {
                            if (recipeAdapter.filter(newText)) {
                                binding.emptyStateFeedGroup.visibility = View.VISIBLE
                            } else {
                                binding.emptyStateFeedGroup.visibility = View.GONE
                            }
                            return true
                        }

                        override fun onQueryTextSubmit(query: String): Boolean {

                            return false
                        }
                    }
                    )
                }

            }.root
    }

}
