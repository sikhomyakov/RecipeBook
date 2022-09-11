package ru.netology.recipesbook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.recipesbook.activity.EditRecipeFragment
import ru.netology.recipesbook.activity.NewRecipeFragment
import ru.netology.recipesbook.adapter.FilterInteractionListener
import ru.netology.recipesbook.adapter.RecipeInteractionListener
import ru.netology.recipesbook.adapter.StepInteractionListener
import ru.netology.recipesbook.db.AppDb
import ru.netology.recipesbook.dto.Categories
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.SingleLiveEvent
import ru.netology.recipesbook.repository.RecipeRepositoryImpl


class RecipeViewModel(application: Application) :
    RecipeInteractionListener,
    StepInteractionListener,
    FilterInteractionListener,
    AndroidViewModel(application) {

    private val repository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )

    val data by repository::data


    val navigateToNewRecipe = SingleLiveEvent<Recipe>()
    val navigateToRecipe = SingleLiveEvent<Recipe>()
    val navigateToEditRecipe = SingleLiveEvent<Boolean>()
    val navigateToFavoriteRecipes = SingleLiveEvent<Boolean>()
    val navigateToAllRecipes = SingleLiveEvent<Boolean>()

    val newRecipe = MutableLiveData<Recipe>()
    val newRecipeImg = MutableLiveData<String>()
    val newStepImg = MutableLiveData<String>()

    val editingRecipe = MutableLiveData<Recipe>()
    val editRecipeImg = MutableLiveData<String>()
    val editStepImg = MutableLiveData<String>()

    val listOfFilters = MutableLiveData<MutableList<Categories>>(mutableListOf())
    val filteredRecipes = MutableLiveData<List<Recipe>>(mutableListOf())
    val filterRecipes = SingleLiveEvent<Boolean>()
    override val filterCheckboxUpdate = MutableLiveData(false)

    fun create(recipe: Recipe) {
        repository.addRecipe(recipe)
    }

    fun edit(recipe: Recipe) {
        repository.updateById(recipe)
    }

    fun add() {
        newRecipe.value = Recipe(
            id = 0,
            author = "",
            title = "",
            content = "",
            published = "",
            recipeImg = "",
            steps = mutableMapOf(),
            categories = mutableSetOf()
        )
        newRecipeImg.value = ""
        newStepImg.value = ""

        navigateToNewRecipe.call()
    }

    fun onApplyFiltersButtonClicked() {
        filteredRecipes.value = data.value
        val recipes = if (!listOfFilters.value.isNullOrEmpty()) {
            filteredRecipes.value?.filter { recipe ->
                recipe.categories.any { category ->
                    category in (listOfFilters.value?.toList() ?: mutableListOf())
                }
            }
        } else {
            data.value
        }

        filteredRecipes.value = recipes ?: data.value
        filterRecipes.call()
    }

    fun onResetFiltersButtonClicked() {
        filteredRecipes.value = data.value


        listOfFilters.value = mutableListOf()
        filterRecipes.call()
    }

    override fun onLikeClicked(recipe: Recipe) {
        repository.likeById(recipe.id)
    }

    override fun onShareClicked(recipe: Recipe) {
        repository.shareById(recipe.id)
    }

    override fun onRemoveClicked(recipeId: Long) {
        repository.deleteById(recipeId)
    }

    override fun onFavoriteClicked(recipe: Recipe) {
        repository.favoriteById(recipe.id)
    }

    override fun onEditClicked(recipeId: Long) {
        editingRecipe.value = repository.findRecipeById(recipeId)
        navigateToEditRecipe.value = true
    }

    override fun onRecipeClicked(recipe: Recipe) {
        navigateToRecipe.value = recipe
    }

    override fun onDeleteStepButtonClicked(recipe: Recipe, stepKey: String, caller: String) {
        val newRecipeSteps = recipe.steps
            .filterNot { step ->
                step.key == stepKey
            }
            .toMutableMap()

        if (caller == NewRecipeFragment.CALLER_NEW_RECIPE) {
            newRecipe.value = recipe.copy(steps = newRecipeSteps)
        } else if (caller == EditRecipeFragment.CALLER_EDIT_RECIPE) {
            editingRecipe.value = recipe.copy(steps = newRecipeSteps)
        }
    }

    override fun onCheckClicked(category: Categories) {
        val filters = listOfFilters.value
        filters?.add(category)
        listOfFilters.value = filters ?: mutableListOf()
    }

    override fun onUncheckClicked(category: Categories) {
        val filters = listOfFilters.value
        filters?.remove(category)
        listOfFilters.value = filters ?: mutableListOf()
    }
}

