package com.example.doggoislovedoggoislifetheapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.doggoislovedoggoislifetheapp.components.DogPicturesViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun BreedFavoritesScreen(
    navController: NavController,
    viewModel: DogPicturesViewModel
) {
    var likedPictures by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var selectedBreed by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(viewModel) {
        likedPictures = viewModel.likedPictures
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp)
        ) {
            // Display a bar to sort for breeds on top of screen
            BreedBar(
                breeds = likedPictures.values.toList(),
                onBreedSelected = { breed ->
                    selectedBreed = breed
                }
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(likedPictures.filter { it.value == selectedBreed }.keys.toList()) { imageUrl ->
                    Spacer(modifier = Modifier.height(15.dp))
                    LikedDogPictureItem(
                        imageUrl = imageUrl,
                        breed = selectedBreed.orEmpty(), // Provide selected breed
                        onUnlikeClick = {
                            // Makes sure the unliked picture is removed immediately
                            likedPictures = likedPictures - it
                            // Remove the picture from likedPictures using the ViewModel
                            viewModel.removeLikedPicture(it)
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    // Navigate back to DogPicturesScreen
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Back to Dog Pictures")
            }
        }
    }
}

// Bar for sorting on breeds
@Composable
fun BreedBar(
    breeds: List<String>,
    onBreedSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        items(breeds.distinct()) { breed ->
            Button(
                onClick = {
                    onBreedSelected(breed)
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(breed.replaceFirstChar { it.uppercase() })
            }
        }
    }
}

@Composable
fun LikedDogPictureItem(
    imageUrl: String,
    breed: String,
    onUnlikeClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

       Box(
           modifier = Modifier
               .fillMaxWidth()
               .background(Color.Gray.copy(alpha = 0.7f))
               .align(Alignment.TopStart)
       ){
        Text(
            text = breed.replaceFirstChar { it.uppercase() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            color = Color.White
        )
       }
        Button(
            onClick = {
                onUnlikeClick(imageUrl)
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text("Unlike")
        }
    }
}



