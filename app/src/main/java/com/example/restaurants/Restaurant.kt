package com.example.restaurants

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Annotate the Restaurant data class fields with special serialization matchers that
tell Retrofit which keys should be matched with each field.
 */
//Todo: instruct Room that the Restaurant data class is an entity that must be saved to our local db
@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey()
    @ColumnInfo(name = "r_id")
    @SerializedName("r_id")
    val id: Int,

    @ColumnInfo(name = "r_title")
    @SerializedName("r_title")
    val title: String,

    @ColumnInfo(name = "r_description")
    @SerializedName("r_description")
    val description: String,

    // used val instead of var to prevent its value from being changed once the object
    // has been created
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false)
