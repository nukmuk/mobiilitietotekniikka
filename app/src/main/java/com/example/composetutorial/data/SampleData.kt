package com.example.composetutorial.data

import com.example.composetutorial.Message

/**
 * SampleData for Jetpack Compose Tutorial
 */
object SampleData {
    // Sample conversation data
    val conversationSample = listOf(
        Message(
            author = "Lexi",
            body = "Test...Test...Test..."
        ),
        Message(
            author = "Lexi",
            body = """List of Android versions:
            |Android KitKat (API 19)
            |Android Lollipop (API 21)
            |Android Marshmallow (API 23)
            |Android Nougat (API 24)
            |Android Oreo (API 26)
            |Android Pie (API 28)
            |Android 10 (API 29)
            |Android 11 (API 30)
            |Android 12 (API 31)""".trim()
        ),
        Message(
            author = "Lexi",
            body = """I think Kotlin is my favorite programming language.
            |It's so much fun!""".trim()
        ),
        Message(
            author = "Lexi",
            body = "Searching for alternatives to XML layouts..."
        ),
        Message(
            author = "Lexi",
            body = """Hey, take a look at Jetpack Compose, it's great!
            |It's the Android's modern toolkit for building native UI.
            |It simplifies and accelerates UI development on Android.
            |Less code, powerful tools, and intuitive Kotlin APIs :)""".trim()
        ),
        Message(
            author = "Lexi",
            body = "It's available from API 21+ :)"
        ),
        Message(
            author = "Lexi",
            body = "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            author = "Lexi",
            body = "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            author = "Lexi",
            body = "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            author = "Lexi",
            body = "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            author = "Lexi",
            body = "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            author = "Lexi",
            body = "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            author = "Lexi",
            body = "Have you tried writing build.gradle with KTS?"
        ),
    )
}