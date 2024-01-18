package com.example.doggoislovedoggoislifetheapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.doggoislovedoggoislifetheapp.network.DogApiResponseRandom
import com.example.doggoislovedoggoislifetheapp.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun DogPicturesScreen(navController: NavController) {
    var dogPictures by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(true) {
        dogPictures = fetchDogPictures(5)
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
            Text(
                text = "What Breed Should We Look At Today?",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            LazyColumn (
                modifier = Modifier.weight(1f)
            ){
                items(dogPictures) { imageUrl ->
                    Spacer(modifier = Modifier.height(15.dp))
                    DogPictureItem(imageUrl = imageUrl, navController)
                }
            }

            // Button which will refresh the dog pictures.
            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    coroutineScope.launch {
                        dogPictures = fetchDogPictures(5)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            ) {
                Text("Fetch New Dog Breeds")
            }
        }
    }
}

@Composable
fun DogPictureItem(imageUrl: String, navController: NavController) {
    val breedName = extractBreedName(imageUrl)
    val localBreedName = breedName.replaceFirstChar { it.uppercase() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                openBreedGallery(breedName, navController)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.7f))
                    .align(Alignment.BottomStart)
        ){
            Text(
                text = localBreedName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

private suspend fun fetchDogPictures(count: Int): List<String> {
    val result = mutableListOf<String>()

    repeat(count){
        try {
            val response: DogApiResponseRandom = RetrofitInstance.api.getDogPictures(1)
            result.add(response.message)
            println("Successfully loaded picture")
        } catch (e: Exception) {
            println("Something failed loading pictures: $e")
        }
    }
    return result
}

private fun extractBreedName(imageUrl: String): String {
    val parts = imageUrl.split("/")

    return if(parts.size >= 4) {
        parts[4]
    } else {
        "Unknown breed"
    }
}

private fun openBreedGallery(breedName: String, navController: NavController) {
    navController.navigate("allBreedPics/${breedName}")
}