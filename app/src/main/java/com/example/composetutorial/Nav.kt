package com.example.composetutorial

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composetutorial.data.SampleData
import com.example.composetutorial.ui.theme.screens.Conversation
import com.example.composetutorial.ui.theme.screens.Settings
import com.example.composetutorial.ui.theme.screens.View2

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    db: AppDatabase,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationNames.SETTINGS,
    ) {
        composable(NavigationNames.CONVERSATION) {
            Conversation(SampleData.conversationSample, {
                navController.navigate(NavigationNames.VIEW2) {
                    launchSingleTop = true
                }
            })
        }
        composable(NavigationNames.VIEW2) {
            View2 {
                if (!navController.popBackStack()) {
                    navController.navigate(NavigationNames.CONVERSATION) {
                        launchSingleTop = true
                    }
                }
            }
        }
        composable(NavigationNames.SETTINGS) {
            Settings(
                {
                    navController.navigate(NavigationNames.CONVERSATION)
                },
                db
            )
        }
    }
}

object NavigationNames {
    const val CONVERSATION = "conversation"
    const val VIEW2 = "view2"
    const val SETTINGS = "settings"
}