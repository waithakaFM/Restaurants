package com.example.restaurants

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * whenever we mark a restaurant as a favorite or not a favorite, we need to
apply a partial update on a particular Restaurant object inside our Room database.
The partial update should not replace the entire Restaurant object, but it should only
update its isFavorite field value.
 */
@Entity
class PartialRestaurant(
    @ColumnInfo(name = "r_id")
    val id: Int,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
    )