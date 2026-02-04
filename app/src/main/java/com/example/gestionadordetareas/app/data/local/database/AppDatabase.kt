package com.example.gestionadordetareas.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestionadordetareas.data.local.dao.TareaDao
import com.example.gestionadordetareas.data.local.model.Tarea

@Database(
    entities = [Tarea::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tareaDao(): TareaDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tareas_db"
                ).build().also { instance = it }
            }
        }
    }
}
