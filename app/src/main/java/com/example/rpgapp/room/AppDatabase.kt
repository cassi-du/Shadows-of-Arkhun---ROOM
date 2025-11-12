package com.example.rpgapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.util.Log
import java.io.File

@Database(entities = [PersonagemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personagemDao(): PersonagemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // Se existir um banco pré-existente criado por outra implementação
                // (ex: DatabaseHelper antigo) que tem schema diferente, removemos
                // o arquivo para forçar criação limpa pelo Room.
                try {
                    val dbFile: File = context.getDatabasePath("personagens.db")
                    if (dbFile.exists()) {
                        Log.i("AppDatabase", "Removing pre-existing database file: ${dbFile.absolutePath}")
                        dbFile.delete()
                    }
                } catch (e: Exception) {
                    Log.w("AppDatabase", "Could not remove existing database file: ${e.message}")
                }

                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "personagens.db"
                )
                    // Se o schema local for diferente do esperado, descarta e recria o DB
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = inst
                inst
            }
        }
    }
}
