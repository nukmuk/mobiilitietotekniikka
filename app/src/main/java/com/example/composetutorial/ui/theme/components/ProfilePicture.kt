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
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.example.composetutorial.utils.PROFILE_PICTURE_NAME
import java.io.File

@Composable
fun ProfilePicture(size: Int = 40, fallback: Int, imageVersion: Int = 0) {
    val context = LocalContext.current
    val profilePictureFile = remember { File(context.filesDir, PROFILE_PICTURE_NAME) }
    val profilePictureExists by remember(imageVersion) {
        mutableStateOf(profilePictureFile.exists())
    }

    if (!profilePictureExists) {
        DefaultProfilePicture(fallback, size)
        return
    }

    // borrowed from https://stackoverflow.com/a/79439065
    val model = ImageRequest.Builder(context)
        .data("${profilePictureFile.toURI()}?v=$imageVersion")
        .diskCachePolicy(CachePolicy.DISABLED)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .build()

    AsyncImage(
        model = model,
        contentDescription = "Profile picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
    )
}

@Composable
fun DefaultProfilePicture(painter: Int, size: Int = 40) {
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
