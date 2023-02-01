package com.example.restaurants

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * RestaurantsDb is an abstract class that inherits from RoomDatabase().
This will allow Room to create the actual implementation of the database
behind the scenes and hide all the heavy implementation details from us.
 */

//Changed the schema when we defined a new column for the Restaurants
//table by adding the @ColumnInfo() annotation to the isFavorite field hence version 2
@Database(entities = [Restaurant::class], version = 2, exportSchema = false)
abstract class RestaurantsDb : RoomDatabase() {
    abstract val dao: RestaurantsDao

    companion object{
        @Volatile // Means that writes to this field are immediately made visible to other threads
        private var INSTANCE: RestaurantsDao? = null

        /**
         * Calls the buildDatabase() ,method and gets the Dao object
         * Used a singleton pattern that allows us to hold a static reference to am object so that
         * it lives as long as our application does.
         */
        fun getDaoInstance(context: Context): RestaurantsDao
        {
            //ensure that only one instance of the dao is created at a time.
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }
                return instance
            }
        }

        /**
         * Essentially, this method returns a RestaurantsDb instance
         */
        private fun buildDatabase(context: Context):
                RestaurantsDb = Room.databaseBuilder(
                context.applicationContext,
                RestaurantsDb::class.java,
                "restaurants_database")
                // Room will drop the old contents and tables and provide us with a fresh start.
                .fallbackToDestructiveMigration()
                .build()
    }
}