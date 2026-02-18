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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.composetutorial.R
import java.io.File

const val profile_picture_name = "profile_picture"

@Composable
fun Settings(onNavigateToConversation: () -> Unit) {
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri == null) {
            Log.d("PhotoPicker", "No media selected")
            return@rememberLauncherForActivityResult
        }
        Log.d("PhotoPicker", "Selected URI: $uri")
        saveProfilePicture(uri, context)
    }

    Column(Modifier.padding(8.dp)) {
        Button(onClick = onNavigateToConversation) { Text("Conversation") }
        AsyncImage(
            model = File(context.filesDir, profile_picture_name),
            contentDescription = "Profile picture",
        )
        TextField("hello", {})
        Button(onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) }) {
            Text("Pick photo")
        }
    }
}

fun saveProfilePicture(uri: Uri, context: Context) {
    val resolver = context.contentResolver
    val file = File(context.filesDir, profile_picture_name)
    resolver.openInputStream(uri).use { stream ->
        file.outputStream().use { outputStream ->
            stream?.copyTo(outputStream)
            println("saved to $outputStream")
        }
    }
}

fun profilePictureExists(context: Context): Boolean {
    val file = File(context.filesDir, profile_picture_name)
    return file.exists()
}