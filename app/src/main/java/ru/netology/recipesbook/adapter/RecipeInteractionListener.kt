package ru.netology.recipesbook.adapter

import ru.netology.recipesbook.dto.Recipe

interface RecipeInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onShareClicked(recipe: Recipe)
    fun onRemoveClicked(recipeId: Long)
    fun onFavoriteClicked(recipe: Recipe)
    fun onEditClicked(recipeId: Long)
    fun onRecipeClicked(recipe: Recipe)
}