package com.example.restaurants

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurants.ui.theme.RestaurantsTheme

@Composable
fun RestaurantsScreen(){
    // TODO: Instantiate the ViewModel
    val viewModel: RestaurantsViewModel = viewModel()

//    // TODO: Add a state object to the parent
//    val state: MutableState<List<Restaurant>> =
//        remember {
//            mutableStateOf(viewModel.getRestaurants())
//        }

    // TODO: Using LazyColumn to display restaurants and passing the state's value to the items constructor of LazyColumn
    LazyColumn(
        contentPadding = PaddingValues(
            vertical = 8.dp,
            horizontal = 8.dp
        )
    ) {
        items(viewModel.state.value) { restaurant ->
            RestaurantItem(restaurant){ id ->
                viewModel.toggleFavorite(id)
            }
        }
    }
}

@Composable
// TODO: Hoist the the state up
fun RestaurantItem(item: Restaurant,
                   onClick: (id: Int) -> Unit) {

    //TODO: Add state to the FavoriteIcon
//    val favoriteState = remember {
//        mutableStateOf(false) }

    val icon = if (item.isFavorite)
        Icons.Filled.Favorite
    else
        Icons.Filled.FavoriteBorder

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
                Modifier.weight(0.7f)
            )

            RestaurantIcon(icon, Modifier.weight(0.15f)) {
                onClick(item.id)
            }
        }
    }
}

@Composable
// TODO: Lift the state up and transformed the icon from stateful composable to a stateless one
private fun RestaurantIcon(icon: ImageVector,
                           modifier: Modifier,
                           onClick: () -> Unit = { }){
    Image(imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier.padding(8.dp)
            .clickable {
                onClick()
            }
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

///**
// * Add the favorite icon
// */
//@Composable
//// TODO: Lift the state up and transformed the icon from stateful composable to a stateless one
//private fun FavoriteIcon(icon: ImageVector,
//                         modifier: Modifier,
//                         onClick: () -> Unit) {
//
//    Image(
//        imageVector = icon,
//        contentDescription = "Favorite restaurant icon",
//        modifier = modifier.padding(8.dp)
//            .clickable {
//                onClick()
//            }
//    )
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RestaurantsTheme {
        RestaurantsScreen()
    }
}