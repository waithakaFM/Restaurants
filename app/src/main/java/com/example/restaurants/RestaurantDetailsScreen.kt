package com.example.restaurants

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * composable screen that will display the details about
a specific restaurant
 */
@Composable
fun RestaurantDetailsScreen() {
    val viewModel: RestaurantDetailsViewModel =
        viewModel()

    val item = viewModel._state.value
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            horizontalAlignment =
            Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            RestaurantIcon(
                Icons.Filled.Place,
                Modifier.padding(
                    top = 32.dp,
                    bottom = 32.dp
                )
            )
            item.title?.let { title ->
                item.description?.let { description ->
                    RestaurantDetails(
                        title,
                        description,
                        Modifier.padding(bottom = 32.dp),
                        Alignment.CenterHorizontally
                    )
                }
            }
            Text("More info coming soon!")
        }

        if (item.isLoading)
            CircularProgressIndicator()
        if(item.error != null)
            Text(text = item.error)
    }
}