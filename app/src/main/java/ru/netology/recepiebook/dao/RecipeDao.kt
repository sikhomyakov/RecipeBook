package ru.netology.recepiebook.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.recepiebook.dto.Recipe
import ru.netology.recepiebook.dto.RecipeEntity

@Dao
interface RecipeDao {

    @Query("""SELECT * FROM RecipeEntity ORDER BY id DESC""")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Query(
        """
           UPDATE RecipeEntity SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )
    fun likedById(id: Long)

    @Query(
        """
           UPDATE RecipeEntity SET
               shares = shares + 1
           WHERE id = :id;
        """
    )
    fun shareById(id: Long)

    @Query(
        """
           UPDATE RecipeEntity SET
               favoriteByMe = CASE WHEN favoriteByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )
    fun favoriteById(id: Long)

    @Insert
    fun insert(recipe: RecipeEntity)

    @Query("UPDATE RecipeEntity SET content = :content, video = :video WHERE id = :id")
    fun updateContentById(id: Long, content: String, video: String)

    fun addRecipe(recipe: RecipeEntity) =
        if (recipe.id == 0L) insert(recipe) else recipe.video?.let {
            updateContentById(
                recipe.id, recipe.content,
                it
            )
        }


    @Query("""DELETE FROM RecipeEntity WHERE id = :id""")
    fun deleteById(id: Long)

    @Query("""SELECT * FROM RecipeEntity WHERE id = :id""")
    fun findRecipeById(id: Long): Recipe
}