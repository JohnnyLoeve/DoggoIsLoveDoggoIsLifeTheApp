import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.doggoislovedoggoislifetheapp.components.DogPicture
import com.example.doggoislovedoggoislifetheapp.components.DogPicturesViewModel
import com.example.doggoislovedoggoislifetheapp.network.DogApiResponseSpecific
import com.example.doggoislovedoggoislifetheapp.network.RetrofitInstanceSpecific

@Composable
fun AllBreedPicsScreen(
    breedName: String,
    navController: NavController,
    viewModel: DogPicturesViewModel
) {
    var imageUrl = breedName
    var dogPictures by remember { mutableStateOf<List<DogPicture>>(emptyList()) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(true) {
        dogPictures = fetchAllBreedPics(imageUrl).map { DogPicture(it) }
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
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = lazyListState
            ) {
                // Creates items containing all the different pictures of the breed.
                items(dogPictures) { dogPicture ->
                    Spacer(modifier = Modifier.height(15.dp))
                    DogPictureItem(
                        dogPicture = dogPicture,
                        // Adds the picture to a list containing liked pictures
                        onLikeClick = { liked ->
                            dogPicture.isLiked.value = liked
                        },
                        viewModel = viewModel
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FavoritesButton(navController)
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Choose Another Breed")
                }
            }
        }
    }
}

// Creates the dogPictureItem, which holds the picture and button to add to list of liked pictures
@Composable
fun DogPictureItem(
    dogPicture: DogPicture,
    onLikeClick: (Boolean) -> Unit,
    viewModel: DogPicturesViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = dogPicture.imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        // Like/Unlike button
        Button(
            onClick = {
                // Extracts the breed from the image URL
                val breedRegex = Regex("breeds/([^/]+)/")
                val matchResult = breedRegex.find(dogPicture.imageUrl)
                val breed = matchResult?.groupValues?.get(1) ?: ""

                // Toggle the liked status when the button is clicked
                if (dogPicture.isLiked.value) {
                    viewModel.removeLikedPicture(dogPicture.imageUrl)
                } else {
                    viewModel.addLikedPicture(dogPicture.imageUrl, breed)
                }
                onLikeClick(!dogPicture.isLiked.value)
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        ) {
            Text(if (dogPicture.isLiked.value) "Unlike" else "Like")
        }
    }
}




private suspend fun fetchAllBreedPics(breedName: String): List<String> {
    val modifiedBreedName = breedName.substringBefore('-')

    try {
        val response: DogApiResponseSpecific = RetrofitInstanceSpecific.api.getDogPicturesSpecific(modifiedBreedName)


        return response.message
    } catch (e: Exception) {
    println("Something went wrong loading breed: $e")

    // If something goes wrong, handle the error appropriately
}
    return emptyList()
}

@Composable
fun FavoritesButton(navController: NavController) {
    Button(
        onClick = {
            // Navigate to the BreedFavoritesScreen
            navController.navigate("breedFavorites")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .zIndex(1f)
    ) {
        Text("Go to Favorites")
    }
}

/*
@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Scroll to Top")
    }
} */

