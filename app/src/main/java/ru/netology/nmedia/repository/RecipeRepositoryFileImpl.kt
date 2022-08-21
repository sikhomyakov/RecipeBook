package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Recipe
import ru.netology.nmedia.dto.Utils

class RecipeRepositoryFileImpl(
    private val context: Context
) : RecipeRepository {
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Recipe::class.java).type
    private val filename = "recipes.json"
    private var recipes = emptyList<Recipe>()
    private val data = MutableLiveData(recipes)
    private var nextId = 1L

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                recipes = gson.fromJson(it, type)
                data.value = recipes
                nextId = if (recipes.isEmpty()) 1 else (recipes.first().id + 1)
            }

        } else sync()
    }


    override fun getAll(): LiveData<List<Recipe>> = data

    override fun save(recipe: Recipe) {
        if (recipe.id == 0L) {

            recipes = listOf(
                recipe.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = Utils.addLocalDataTime()
                )
            ) + recipes
            data.value = recipes
            sync()
            return
        }

        recipes = recipes.map {
            if (it.id != recipe.id) it else it.copy(content = recipe.content)
        }
        data.value = recipes
        sync()
    }

    override fun likeById(id: Long) {
        recipes = recipes.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = recipes
        sync()
    }

    override fun shareById(id: Long) {
        recipes = recipes.map {
            if (it.id == id) it.copy(shares = it.shares + 1) else it
        }
        data.value = recipes
        sync()
    }

    override fun deleteById(id: Long) {
        recipes = recipes.filter { it.id != id }
        data.value = recipes
        sync()
    }

    override fun favoriteById(id: Long) {
        recipes = recipes.map {
            if (it.id != id) it else it.copy(
                favByMe = !it.favByMe
            )
        }
        data.value = recipes
        sync()
        }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(recipes))
        }
    }
}