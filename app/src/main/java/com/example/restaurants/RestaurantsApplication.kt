package com.example.restaurants

import android.app.Application
import android.content.Context

/**
 * This class now inherits from android.app.Application and exposes its
context through the static getAppContext() method
It provides a way to access the application context of the app  without having
to pass the context explicitly as an argument to each function that needs
 We also add android:name=".RestaurantsApplication" to the manifest.xml
 */
class RestaurantsApplication: Application() {
    init { app = this }
    companion object {
        private lateinit var app: RestaurantsApplication
        fun getAppContext(): Context = app.applicationContext
    }
}