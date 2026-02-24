package com.example.composetutorial


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import com.example.composetutorial.utils.createNotificationChannel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startForegroundService(Intent(this, MotionDetectionService::class.java))
        createNotificationChannel(this)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "my-database"
            ).build()
            val userDao = db.userDao()
            LaunchedEffect(Unit) {
                val user = userDao.get(0)
                if (user == null)
                    userDao.upsertUser(User(0, "Lexi"))
            }

            LaunchedEffect(navController) {
                val destination = intent?.getStringExtra("navigate_to") ?: return@LaunchedEffect
                navController.navigate(destination) { launchSingleTop = true }
            }

            ComposeTutorialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyAppNavHost(
                        modifier = Modifier.statusBarsPadding(),
                        navController = navController,
                        db = db,
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
