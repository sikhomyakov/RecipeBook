package ru.netology.recipesbook.dto

import kotlinx.serialization.Serializable
import ru.netology.recipesbook.R
import ru.netology.recipesbook.activity.App

@Serializable
data class Recipe(
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

enum class Categories(val categoryName: String) {
    EUROPEAN(App.appContext.getString(R.string.category_European)),
    ASIAN(App.appContext.getString(R.string.category_Asian)),
    PANASIAN(App.appContext.getString(R.string.category_PanAsian)),
    ORIENTAL(App.appContext.getString(R.string.category_Oriental)),
    AMERICAN(App.appContext.getString(R.string.category_American)),
    RUSSIAN(App.appContext.getString(R.string.category_Russian)),
    MEDITERRANEAN(App.appContext.getString(R.string.category_Mediterranean))
}
