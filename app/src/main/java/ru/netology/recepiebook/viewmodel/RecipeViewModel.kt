package ru.netology.recepiebook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.recepiebook.db.AppDb
import ru.netology.recepiebook.dto.Recipe
import ru.netology.recepiebook.dto.Utils
import ru.netology.recepiebook.repository.RecipeRepository
import ru.netology.recepiebook.repository.RecipeRepositorySQLiteImpl


private val empty = Recipe(
    id = 0,
    content = "",
    author = "Me",
    likedByMe = false,
    published = Utils.addLocalDataTime(),
    likes = 0,
    shares = 0,
    video = "",
    favoriteByMe = false,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository =
        RecipeRepositorySQLiteImpl(AppDb.getInstance(application).recipeDao)
    val data = repository.getAll()
    private val edited = MutableLiveData(empty)
    fun save() {
        edited.value?.let {
            repository.addRecipe(it)
        }
        edited.value = empty
    }

    fun edit(recipe: Recipe) {
        edited.value = recipe
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun deleteById(id: Long) = repository.deleteById(id)
    fun favoriteById(id: Long) = repository.favoriteById(id)
}
