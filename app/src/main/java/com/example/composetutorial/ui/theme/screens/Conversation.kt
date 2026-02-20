package com.example.composetutorial.ui.theme.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetutorial.R
import com.example.composetutorial.User
import com.example.composetutorial.UserDao
import com.example.composetutorial.data.SampleData
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.composetutorial.ui.theme.components.ProfilePicture

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message, customName: String) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        ProfilePicture(fallback = R.drawable.profile_picture)

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )

        val author = customName

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = author,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>, onNavigateToView2: () -> Unit, userDao: UserDao? = null) {
    var customName by remember { mutableStateOf("")}
    LaunchedEffect(Unit) {
        val existingUser = userDao?.get(0)
        existingUser?.username?.let { customName = it }
    }

    Column {
        Button(onClick = onNavigateToView2, Modifier.padding(8.dp)) {
            Text("Go to view 2")
        }
        LazyColumn {
            items(messages) { message ->
                MessageCard(message, customName)
            }
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    ComposeTutorialTheme {
        Conversation(SampleData.conversationSample, {})
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    ComposeTutorialTheme {
        Surface {
            MessageCard(
                msg = Message("Lexi", "Hey, take a look at Jetpack Compose, it's great!",),
                "test"
            )
        }
    }
}