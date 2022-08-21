package ru.netology.recepiebook.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var shares: Int = 0,
    var favoriteByMe: Boolean = false,
    val video: String?
) {
    companion object {
        fun fromRecipe(recipe: Recipe): RecipeEntity {
            return with(recipe) {
                RecipeEntity(
                    id,
                    author,
                    published,
                    content,
                    likedByMe,
                    likes,
                    shares,
                    favoriteByMe,
                    video
                )
            }
        }
    }
}

fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        id,
        author,
        published,
        content,
        likedByMe,
        likes,
        shares,
        favoriteByMe,
        video

    )
}