package com.example.composetutorial.utils

import android.content.Context
import android.net.Uri
import java.io.File

const val PROFILE_PICTURE_NAME = "profile_picture"

fun getProfilePictureFile(context: Context): File {
    return File(context.filesDir, PROFILE_PICTURE_NAME)
}

fun saveProfilePicture(uri: Uri, context: Context) {
    val profilePictureFile = getProfilePictureFile(context)
    val resolver = context.contentResolver
    resolver.openInputStream(uri)?.use { stream ->
        profilePictureFile.outputStream().use { outputStream ->
            stream.copyTo(outputStream)
        }
    }
}

fun deleteProfilePicture(context: Context) {
    val profilePictureFile = getProfilePictureFile(context)
    profilePictureFile.delete()
}

