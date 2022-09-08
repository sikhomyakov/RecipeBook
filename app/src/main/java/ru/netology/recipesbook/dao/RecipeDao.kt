package ru.netology.recipesbook.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.recipesbook.dto.Categories
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.RecipeEntity

@Dao
interface RecipeDao {

    @Query("""SELECT * FROM recipes ORDER BY id DESC""")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query(
        """
           UPDATE recipes SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )
    fun likedById(id: Long)

    @Query(
        """
           UPDATE recipes SET
               shares = shares + 1
           WHERE id = :id;
        """
    )
    fun shareById(id: Long)

    @Query(
        """
           UPDATE recipes SET
               favoriteByMe = CASE WHEN favoriteByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )
    fun favoriteById(id: Long)

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query(
        "UPDATE recipes SET " +
                "title = :title, " +
                "content = :content, " +
                "recipeImg = :recipeImg, " +
                "steps = :steps, " +
                "categories = :categories  " +
                "WHERE id = :id"
    )
    fun update(
        id: Long,
        title: String,
        content: String,
        recipeImg: String?,
        steps: MutableMap<String, String>,
        categories: MutableSet<Categories>
    )

    @Query("""DELETE FROM recipes WHERE id = :id""")
    fun deleteById(id: Long)

    @Query("""SELECT * FROM recipes WHERE id = :id""")
    fun findRecipeById(id: Long): Recipe
}