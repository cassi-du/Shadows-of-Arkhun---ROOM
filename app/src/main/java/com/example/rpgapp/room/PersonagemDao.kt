package com.example.rpgapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonagemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(personagem: PersonagemEntity): Long

    @Query("SELECT * FROM personagens ORDER BY nome")
    fun getAll(): List<PersonagemEntity>

    @Query("SELECT COUNT(1) FROM personagens WHERE nome = :name")
    fun countByName(name: String): Int
}
