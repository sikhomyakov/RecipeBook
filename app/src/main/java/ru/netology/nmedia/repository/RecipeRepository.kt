package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Recipe

interface RecipeRepository {
    fun getAll(): LiveData<List<Recipe>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun deleteById(id: Long)
    fun favoriteById(id: Long)
    fun save(recipe: Recipe)
}