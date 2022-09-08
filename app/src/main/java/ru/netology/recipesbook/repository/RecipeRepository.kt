package ru.netology.recipesbook.repository

import androidx.lifecycle.LiveData
import ru.netology.recipesbook.dto.Recipe

interface RecipeRepository {

    val data: LiveData<List<Recipe>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun deleteById(id: Long)
    fun favoriteById(id: Long)
    fun addRecipe(recipe: Recipe)
    fun updateById(recipe: Recipe)
    fun findRecipeById(id: Long): Recipe
}