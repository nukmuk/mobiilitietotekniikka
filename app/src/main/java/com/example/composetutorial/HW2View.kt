package com.example.composetutorial

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HW2View(onBackButton: () -> Unit
) {
    Row {
        Button(onClick = onBackButton) {
            Text("back")
        }
        Text("View 2")
    }
}

@Preview
@Composable
fun HW2ViewPreview() {
    HW2View {}
}