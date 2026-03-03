package com.example.composetutorial

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Message::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
}