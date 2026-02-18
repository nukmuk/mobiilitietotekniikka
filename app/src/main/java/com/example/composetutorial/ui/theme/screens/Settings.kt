package com.example.composetutorial.ui.theme.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composetutorial.R
import com.example.composetutorial.ui.theme.components.ProfilePicture
import java.io.File

const val profile_picture_name = "profile_picture"

@Composable
fun Settings(onNavigateToConversation: () -> Unit) {
    val context = LocalContext.current
    var imageVersion by remember { mutableIntStateOf(0) }
    val profilePictureFile = remember { File(context.filesDir, profile_picture_name) }
    val profilePictureExists by remember(imageVersion) {
        mutableStateOf(profilePictureFile.exists())
    }

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri == null) {
            Log.d("PhotoPicker", "No media selected")
            return@rememberLauncherForActivityResult
        }
        Log.d("PhotoPicker", "Selected URI: $uri")
        saveProfilePicture(uri, context, profilePictureFile)
        imageVersion++
    }

    Column(Modifier.padding(8.dp)) {
        Button(onClick = onNavigateToConversation) { Text("Conversation") }
        if (profilePictureExists)
            ProfilePicture("${profilePictureFile.absolutePath}?v=$imageVersion", 120)
        else
            ProfilePicture(R.drawable.profile_picture, 120)
        TextField("hello", {})
        Button(onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) }) {
            Text("Pick photo")
        }
    }
}

fun saveProfilePicture(uri: Uri, context: Context, profilePictureFile: File) {
    val resolver = context.contentResolver
    resolver.openInputStream(uri).use { stream ->
        profilePictureFile.outputStream().use { outputStream ->
            stream?.copyTo(outputStream)
            println("saved profile picture")
        }
    }
}
