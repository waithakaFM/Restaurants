package com.example.restaurants

/**
 * Deep links in Android are links that take users directly to a specific location within an app,
 * rather than just launching the app. They are similar to URLs on the web, but instead of taking
 * users to a website, they take users to a specific screen or piece of content within an app.
 */
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.restaurants.ui.theme.RestaurantsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantsTheme {
                RestaurantsApp()
            }
        }
    }

    /**
     * This composable function will act as the parent composable function of our
    Restaurants application. Here, all the screens of the app will be defined.
     */
    @Composable
    private fun RestaurantsApp() {
        // handles the navigation between composable screens
        // it operates on the back stack of composable destinations
        val navController = rememberNavController()

        //container composable that will display the composable destinations
        NavHost(navController, startDestination = "restaurants") {
            composable(route = "restaurants"){
                RestaurantsScreen { id ->
                    navController.navigate("restaurants/$id")
                }
            }
            composable(route = "restaurants/{restaurant_id}",
                arguments = listOf(navArgument("restaurant_id") {
                    type = NavType.IntType
                }),
                deepLinks = listOf(navDeepLink {
                    uriPattern =
                        "www.restaurantsapp.details.com/{restaurant_id}"
                })
            ) {
                RestaurantDetailsScreen()
            }
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        RestaurantsTheme {
            RestaurantsApp()
        }
    }

}

//The arguments attribute is used to specify the arguments that will be passed to the composable
// function.
// In this case, the restaurant_id argument is passed as an integer to the RestaurantDetailsScreen
// and it's used to identify the restaurant.

