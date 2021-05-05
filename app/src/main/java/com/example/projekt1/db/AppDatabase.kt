package com.example.projekt1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projekt1.models.LocationResponse

@Database(entities = [LocationResponse::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): LocationDao

}