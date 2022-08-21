package ru.netology.recepiebook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.recepiebook.dao.RecipeDao
import ru.netology.recepiebook.dto.Recipe


class RecipeRepositorySQLiteImpl(
    private val dao: RecipeDao
) : RecipeRepository {
    private var recepies = emptyList<Recipe>()
    private val data = MutableLiveData(recepies)

    init {
        recepies = dao.getAll()
        data.value = recepies
    }

    override fun getAll(): LiveData<List<Recipe>> = data

    override fun addRecipe(recipe: Recipe) {
        val id = recipe.id
        val saved = dao.addRecipe(recipe)
        recepies = if (id == 0L) {
            listOf(saved) + recepies
        } else {
            recepies.map {
                if (it.id != id) it else saved
            }
        }
        data.value = recepies
    }

    override fun likeById(id: Long) {
        dao.likedById(id)
        recepies = recepies.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = recepies
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        recepies = recepies.map {
            if (it.id != id) it else it.copy(
                shares = it.shares + 1
            )
        }
        data.value = recepies
    }

    override fun favoriteById(id: Long) {
        dao.favoriteById(id)
        recepies = recepies.map {
            if (it.id != id) it else it.copy(
                favoriteByMe = !it.favoriteByMe,
            )
        }
        data.value = recepies
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        recepies = recepies.filter { it.id != id }
        data.value = recepies
    }

    override fun findRecipeById(id: Long): Recipe {
        dao.findRecipeById(id)
        return recepies.first { it.id == id }
    }
}