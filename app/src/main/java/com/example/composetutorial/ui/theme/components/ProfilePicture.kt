package com.example.composetutorial.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.io.File

@Composable
fun ProfilePicture(profilePictureFile: File, imageVersion: Int, size: Int = 40, fallback: Int) {
    val profilePictureExists by remember(imageVersion) {
        mutableStateOf(profilePictureFile.exists())
    }

    val context = LocalContext.current

    println("model: ${profilePictureFile.absolutePath}, size: $size, exists: $profilePictureExists, fb: $fallback, version: $imageVersion")

    if (!profilePictureExists) {
        ProfilePicture(fallback, size)
        return
    }
    val imageRequest = remember(imageVersion) {
        ImageRequest.Builder(context)
            .data(profilePictureFile)
            .memoryCacheKey("profile_picture_$imageVersion")
            .diskCacheKey("profile_picture_$imageVersion")
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = "Profile picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape))

}


@Composable
fun ProfilePicture(painter: Int, size: Int = 40) {
    Image(
        painter = painterResource(painter),
        contentDescription = "Profile picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}