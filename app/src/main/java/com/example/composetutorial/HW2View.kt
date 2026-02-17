package com.example.composetutorial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HW2View(onBackButton: () -> Unit
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
fun HW2ViewPreview() {
    HW2View {}
}