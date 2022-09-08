package ru.netology.recipesbook.repository


import androidx.lifecycle.map
import ru.netology.recipesbook.dao.RecipeDao
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.toRecipe
import ru.netology.recipesbook.dto.toRecipeEntity


class RecipeRepositoryImpl(
    private val dao: RecipeDao
) : RecipeRepository {

    override val data = dao.getAll().map { listOfEntities ->
        listOfEntities.map { recipeEntity ->
            recipeEntity.toRecipe()
        }
    }

    override fun addRecipe(recipe: Recipe) {
        dao.insert(recipe.toRecipeEntity())
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

    override fun updateById(recipe: Recipe) {
        dao.update(
            id = recipe.id,
            title = recipe.title,
            content = recipe.content,
            recipeImg = recipe.recipeImg,
            steps = recipe.steps,
            categories = recipe.categories
        )
    }

    override fun findRecipeById(id: Long): Recipe {
        return dao.findRecipeById(id)
    }


}
