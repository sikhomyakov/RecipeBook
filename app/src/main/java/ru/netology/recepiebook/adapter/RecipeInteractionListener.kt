package ru.netology.recepiebook.adapter

import ru.netology.recepiebook.dto.Recipe

interface RecipeInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onShareClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onFavoriteClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onVideoClicked(recipe: Recipe) {}
    fun onRecipeClicked(recipe: Recipe)
}