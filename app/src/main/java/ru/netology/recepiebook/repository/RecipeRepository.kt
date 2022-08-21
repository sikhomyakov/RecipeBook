package ru.netology.recepiebook.repository

import androidx.lifecycle.LiveData
import ru.netology.recepiebook.dto.Recipe

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun deleteById(id: Long)
    fun favoriteById(id: Long)
    fun addRecipe(recipe: Recipe)
    fun findRecipeById(id: Long): Recipe
}