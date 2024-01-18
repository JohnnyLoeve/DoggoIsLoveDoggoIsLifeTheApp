package com.example.doggoislovedoggoislifetheapp

import AllBreedPicsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.doggoislovedoggoislifetheapp.components.DogPicturesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val dogPicturesViewModel: DogPicturesViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "dogPictures"
            ) {
                composable("dogPictures") {
                    DogPicturesScreen(navController = navController)
                }

                composable(
                    route = "allBreedPics/{imageUrl}",
                    arguments = listOf(
                        navArgument("imageUrl") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val imageUrl = backStackEntry.arguments?.getString("imageUrl")
                    if (imageUrl != null) {
                        AllBreedPicsScreen(breedName = imageUrl, navController, viewModel = dogPicturesViewModel)
                    }
                }

                composable("breedFavorites") {
                    BreedFavoritesScreen(navController = navController, viewModel = dogPicturesViewModel)
                }

            }
        }
    }
}
