package com.example.composetutorial


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composetutorial.data.SampleData
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.composetutorial.ui.theme.screens.Conversation
import com.example.composetutorial.ui.theme.screens.View2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ComposeTutorialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyAppNavHost(
                        modifier = Modifier.statusBarsPadding(),
                        navController = navController,
                    )
                }
            }
        }
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "conversation",
    ) {
        composable("conversation") {
            Conversation(SampleData.conversationSample, {
                navController.navigate("view2") {
                    launchSingleTop = true
                }
            })
        }
        composable("view2") {
            View2 {
                if (!navController.popBackStack()) {
                    navController.navigate("conversation") {
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

