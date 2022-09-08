package ru.netology.recipesbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import ru.netology.recipesbook.dao.RecipeDao
import ru.netology.recipesbook.db.converters.ListConverter
import ru.netology.recipesbook.db.converters.MapConverter
import ru.netology.recipesbook.db.converters.SetConverter
import ru.netology.recipesbook.dto.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
@TypeConverters(ListConverter::class, MapConverter::class, SetConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .allowMainThreadQueries()
                .build()
    }
}