package ru.netology.recepiebook.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.recepiebook.dto.Recipe
import ru.netology.recepiebook.dto.Utils

class RecipeDaoImpl(private val db: SQLiteDatabase) : RecipeDao {

    private val currentUser = "Me"

    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKE_BY_ME} BOOLEAN DEFAULT 0,
            ${PostColumns.COLUMN_NUMBER_OF_LIKES} INTEGER DEFAULT 0,
            ${PostColumns.COLUMN_NUMBER_OF_SHARE} INTEGER DEFAULT 0,
            ${PostColumns.COLUMN_NUMBER_OF_VIEWS} INTEGER DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} TEXT DEFAULT ""
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKE_BY_ME = "likedByMe"
        const val COLUMN_NUMBER_OF_LIKES = "likes"
        const val COLUMN_NUMBER_OF_SHARE = "shares"
        const val COLUMN_NUMBER_OF_VIEWS = "views"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKE_BY_ME,
            COLUMN_NUMBER_OF_LIKES,
            COLUMN_NUMBER_OF_SHARE,
            COLUMN_NUMBER_OF_VIEWS,
            COLUMN_VIDEO
        )
    }

    override fun getAll(): List<Recipe> {
        val posts = mutableListOf<Recipe>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun addRecipe(post: Recipe): Recipe {
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            put(PostColumns.COLUMN_AUTHOR, currentUser)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, Utils.addLocalDataTime())
            put(PostColumns.COLUMN_VIDEO, post.video)
        }
        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)

        }
    }

    override fun likedById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               shares = shares + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun favoriteById(id: Long) {
        db.execSQL(
            """
                UPDATE  posts SET
                views = views + 1
                WHERE  id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun deleteById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun findRecipeById(id: Long): Recipe {
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    private fun map(cursor: Cursor): Recipe {
        with(cursor) {
            return Recipe(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKE_BY_ME)) != 0,
                likes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_NUMBER_OF_LIKES)),
                shares = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_NUMBER_OF_SHARE)),
                video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
            )
        }
    }
}