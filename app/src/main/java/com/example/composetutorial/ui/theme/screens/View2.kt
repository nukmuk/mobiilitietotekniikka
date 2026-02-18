package com.example.composetutorial.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun View2(onBackButton: () -> Unit
) {
    Column (Modifier.padding(8.dp)){
        Button(onClick = onBackButton) {
            Text("back")
        }
        Text("View 2")
    }
}

@Preview
@Composable
fun View2Preview() {
    View2 {}
}