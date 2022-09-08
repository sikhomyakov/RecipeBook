package ru.netology.recipesbook.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val title: String,
    val content: String,
    val recipeImg: String,
    val steps: MutableMap<String, String>,
    val categories: MutableSet<Categories>,
    var likes: Int = 0,
    var shares: Int = 0,
    var likedByMe: Boolean = false,
    var favoriteByMe: Boolean = false,
)

internal fun Recipe.toRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        id,
        author,
        published,
        title,
        content,
        recipeImg,
        steps,
        categories,
        likes,
        shares,
        likedByMe,
        favoriteByMe,
    )
}


internal fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        id,
        author,
        published,
        title,
        content,
        recipeImg,
        steps,
        categories,
        likes,
        shares,
        likedByMe,
        favoriteByMe,
    )
}