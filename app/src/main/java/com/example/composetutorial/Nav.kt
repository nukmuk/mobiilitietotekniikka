package com.example.composetutorial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composetutorial.ui.theme.screens.Conversation
import com.example.composetutorial.ui.theme.screens.SensorsScreen
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
        startDestination = Screens.SETTINGS,
    ) {
        composable(Screens.CONVERSATION) {
            val messages by db.messageDao().getAllMessages().collectAsState(initial = emptyList())
            Conversation(
                messages, {
                    navController.navigate(Screens.VIEW2) {
                        launchSingleTop = true
                    }
                },
                userDao = db.userDao(),
                messageDao = db.messageDao()
            )
        }
        composable(Screens.VIEW2) {
            View2 {
                if (!navController.popBackStack()) {
                    navController.navigate(Screens.CONVERSATION) {
                        launchSingleTop = true
                    }
                }
            }
        }
        composable(Screens.SETTINGS) {
            Settings(
                {
                    navController.navigate(Screens.CONVERSATION)
                },
                db,
                { navController.navigate(Screens.SENSORS)}
            )
        }
        composable(Screens.SENSORS){ SensorsScreen() }
    }
}

object Screens {
    const val CONVERSATION = "conversation"
    const val VIEW2 = "view2"
    const val SETTINGS = "settings"
    const val SENSORS = "sensors"
}