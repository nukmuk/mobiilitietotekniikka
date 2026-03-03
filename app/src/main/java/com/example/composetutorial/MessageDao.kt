package com.example.composetutorial

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM Message")
    fun getAllMessages(): Flow<List<Message>>

    @Insert
    suspend fun insert(message: Message)

    @Insert
    suspend fun insertAll(messages: List<Message>)

    @Query("SELECT COUNT(*) FROM Message")
    suspend fun getCount(): Int
}
