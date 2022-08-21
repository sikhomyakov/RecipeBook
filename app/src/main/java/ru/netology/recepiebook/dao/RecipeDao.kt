package ru.netology.recepiebook.dao

import ru.netology.recepiebook.dto.Recipe

interface RecipeDao {
    fun getAll(): List<Recipe>
    fun likedById(id: Long)
    fun shareById(id: Long)
    fun favoriteById(id: Long)
    fun addRecipe(post: Recipe): Recipe
    fun deleteById(id: Long)
    fun findRecipeById(id: Long): Recipe
}