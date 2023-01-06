package com.example.restaurants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restaurants.ui.theme.RestaurantsTheme

@Composable
fun RestaurantsScreen(){
//    // TODO: adding scrolling to the column composable
//    Column(Modifier.verticalScroll(rememberScrollState())) {
//        dummyRestaurants.forEach { restaurant ->
//            RestaurantItem(restaurant)
//        }
//    }

    // TODO: Using LazyColumn to display restaurants
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ) {
        items(dummyRestaurants) { restaurant ->
            RestaurantItem(restaurant)
        }
    }
}

@Composable
fun RestaurantItem(item: Restaurant) {
    // Card is similar to CardView
    Card(elevation = 2.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(verticalAlignment =
        Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)) {
            RestaurantIcon(
                Icons.Filled.Place,
                // RestaurantIcon will take 15% of the horizontal space from its parent Row
                Modifier.weight(0.15f))
            RestaurantDetails(
                item.title,
                item.description,
                Modifier.weight(0.85f)
            )
        }
    }
}

@Composable
private fun RestaurantIcon(icon: ImageVector, modifier: Modifier){
    Image(imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier.padding(8.dp)
    )
}

@Composable
private fun RestaurantDetails(title: String, description: String,
                              modifier: Modifier) {
        Column(modifier = modifier) {
        Text(text = title,
            style = MaterialTheme.typography.h6)
        // to display the description that's faded out in contrast to the title
        // the child Text with the restaurant description will be faded or grayed out
        CompositionLocalProvider(
            LocalContentAlpha provides
                    ContentAlpha.medium) {
            Text(text = description,
                style = MaterialTheme.typography.body2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RestaurantsTheme {
        RestaurantsScreen()
    }
}