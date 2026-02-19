package com.example.composetutorial.ui.theme.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composetutorial.AppDatabase
import com.example.composetutorial.R
import com.example.composetutorial.User
import com.example.composetutorial.ui.theme.components.ProfilePicture
import kotlinx.coroutines.launch
import java.io.File

const val profile_picture_name = "profile_picture"

@Composable
fun Settings(onNavigateToConversation: () -> Unit, db: AppDatabase) {
    val context = LocalContext.current
    var imageVersion by remember { mutableIntStateOf(0) }
    val profilePictureFile = remember { File(context.filesDir, profile_picture_name) }
    val usernameState = rememberTextFieldState()
    val userDao = db.userDao()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val existingUser = userDao.get(0)
        existingUser?.username?.let { usernameState.setTextAndPlaceCursorAtEnd(it) }
    }

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri == null) {
            Log.d("PhotoPicker", "No media selected")
            return@rememberLauncherForActivityResult
        }
        Log.d("PhotoPicker", "Selected URI: $uri")
        if (saveProfilePicture(uri, context, profilePictureFile)) {
            imageVersion++
        }
    }

    Column(Modifier.padding(8.dp)) {
        Button(onClick = onNavigateToConversation) { Text("Conversation") }
        ProfilePicture(profilePictureFile, imageVersion, 120, R.drawable.profile_picture)
        TextField(
            state = usernameState,
            label = { Text("Username") }
        )
        Button(onClick = {
            scope.launch {
                db.userDao().upsertUser(User(0, usernameState.text.toString()))
            }
        }) {
            Text("Save Username")
        }
        Button(onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) }) {
            Text("Pick photo")
        }
        Button(onClick = {
            profilePictureFile.delete()
            imageVersion++
        }) {
            Text("Reset profile picture")
        }
    }
}

fun saveProfilePicture(uri: Uri, context: Context, profilePictureFile: File): Boolean {
    return try {
        val resolver = context.contentResolver
        resolver.openInputStream(uri)?.use { stream ->
            if (!profilePictureFile.exists())
                profilePictureFile.createNewFile()
            profilePictureFile.outputStream().use { outputStream ->
                stream.copyTo(outputStream)
                println("saved profile picture")
            }
        }
        true
    } catch (e: Exception) {
        Log.e("PhotoPicker", "Error saving profile picture", e)
        false
    }
}
