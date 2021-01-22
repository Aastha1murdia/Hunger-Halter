package com.example.hungerhalter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurant")
data class RestaurantEntity(
        @PrimaryKey val restaurantId:Int,
        @ColumnInfo(name = "restaurant_name") val restaurantName: String,
        @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
        @ColumnInfo(name = "restaurant_image") val restaurantImage: String
)


