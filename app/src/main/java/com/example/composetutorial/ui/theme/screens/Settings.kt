package com.example.composetutorial.ui.theme.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.composetutorial.AppDatabase
import com.example.composetutorial.R
import com.example.composetutorial.User
import com.example.composetutorial.ui.theme.components.ProfilePicture
import com.example.composetutorial.utils.createNotificationChannel
import com.example.composetutorial.utils.deleteProfilePicture
import com.example.composetutorial.utils.saveProfilePicture
import com.example.composetutorial.utils.showNotification
import kotlinx.coroutines.launch

@Composable
fun Settings(
    onNavigateToConversation: () -> Unit,
    db: AppDatabase,
    onNavigateToSensors: () -> Unit,
    onNavigateToParkingSpaces: () -> Unit
) {
    val context = LocalContext.current
    val usernameState = rememberTextFieldState("Lexi")
    val userDao = db.userDao()
    val scope = rememberCoroutineScope()
    var imageVersion by remember { mutableIntStateOf(0) }

    // borrowed from https://stackoverflow.com/a/79111223
    var hasNotificationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { result ->
            hasNotificationPermission = result
            // if granted you can show notification here
            if (hasNotificationPermission) showNotification(context)
        }

    // Create notification channel on first launch
    LaunchedEffect(Unit) {
        createNotificationChannel(context)
        val existingUser = userDao.get(0)
        existingUser?.username?.let { usernameState.setTextAndPlaceCursorAtEnd(it) }
    }

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri == null) {
            Log.d("PhotoPicker", "No media selected")
            return@rememberLauncherForActivityResult
        }
        Log.d("PhotoPicker", "Selected URI: $uri")
        saveProfilePicture(uri, context)
        imageVersion++
    }

    Column(Modifier.padding(8.dp)) {
        Button(onClick = onNavigateToConversation) { Text("Conversation") }
        Button(onClick = onNavigateToParkingSpaces) { Text("Parking Spaces") }
        ProfilePicture(
            size = 120,
            fallback = R.drawable.profile_picture,
            imageVersion = imageVersion
        )
        TextField(
            state = usernameState,
            label = { Text("Username") },
            lineLimits = TextFieldLineLimits.SingleLine,
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
            deleteProfilePicture(context)
            imageVersion++

        }) {
            Text("Reset profile picture")
        }

        Button(
            onClick = {
                permissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            enabled = !hasNotificationPermission
        ) {
            Text(if (hasNotificationPermission) "Notification Permission Granted" else "Request Notification Permission")
        }

        Text(
            text = if (hasNotificationPermission) "notification permission granted" else "notification permission not granted",
            color = if (hasNotificationPermission) Color.Green else Color.Red
        )

        Button(
            onClick = { showNotification(context, "Test Notification", "This is a test!") },
            enabled = hasNotificationPermission
        ) {
            Text("Send Test Notification")
        }

        Button(onClick = { onNavigateToSensors() }) { Text("Sensors") }
    }
}
