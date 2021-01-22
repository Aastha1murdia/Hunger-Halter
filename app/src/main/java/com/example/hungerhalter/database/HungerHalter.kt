package com.example.hungerhalter.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class],version = 1)
abstract class HungerHalter: RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
}