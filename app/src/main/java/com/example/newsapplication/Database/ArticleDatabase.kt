package com.example.newsapplication.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapplication.Models.Article


@Database(entities = [Article::class], version = 1)
@TypeConverters(ArticleTypeConvertor::class)
abstract class ArticleDatabase : RoomDatabase() {

    companion object {
        private var INSTANCE: ArticleDatabase? = null
        private val DATABASE_NAME = "article.db"

        @Synchronized
        fun getInstance(context: Context): ArticleDatabase {
            if (INSTANCE == null) {
                synchronized(ArticleDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ArticleDatabase::class.java,
                            DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun articleDao(): ArticleDAO
}