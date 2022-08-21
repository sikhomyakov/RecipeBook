package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Recipe

interface RecipeInteractionListener {
    fun onLikeClicked(recipe: Recipe)
    fun onShareClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onFavoriteClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onVideoClicked(recipe: Recipe) {}
    fun onRecipeClicked(recipe: Recipe)
}