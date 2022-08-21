package ru.netology.recepiebook.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.recepiebook.dao.RecipeDao
import ru.netology.recepiebook.dto.Recipe
import ru.netology.recepiebook.dto.RecipeEntity
import ru.netology.recepiebook.dto.toRecipe


class RecipeRepositorySQLiteImpl(
    private val dao: RecipeDao
) : RecipeRepository {

    override fun getAll(): LiveData<List<Recipe>> =
        Transformations.map(dao.getAll()) { it.map(RecipeEntity::toRecipe) }

    override fun addRecipe(recipe: Recipe) {
        dao.addRecipe(RecipeEntity.fromRecipe(recipe))
    }

    override fun likeById(id: Long) {
        dao.likedById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun favoriteById(id: Long) {
        dao.favoriteById(id)
    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override fun findRecipeById(id: Long): Recipe {
        return dao.findRecipeById(id)
    }


}
