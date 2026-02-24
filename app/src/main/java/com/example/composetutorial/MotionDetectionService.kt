// starting point generated with claude sonnet 4.6, edited and verified manually
package com.example.composetutorial

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.composetutorial.utils.showNotification
import kotlin.math.sqrt

class MotionDetectionService : Service() {

    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null
    private var lastSpinNotificationTime: Long = 0
    private val spinNotificationCooldown: Long = 10000

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            if (event.sensor.type != Sensor.TYPE_GYROSCOPE)
                return

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val rotationAmount = sqrt(x+y+z)

            if (rotationAmount < 1.0f)
                return

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastSpinNotificationTime < spinNotificationCooldown)
                return

            val intent = Intent(this@MotionDetectionService, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("navigate_to", Screens.CONVERSATION)
            }
            val pendingIntent = PendingIntent.getActivity(
                this@MotionDetectionService, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            showNotification(
                this@MotionDetectionService,
                title = "Motion detected",
                message = "Tap to open conversation",
                pendingIntent = pendingIntent
            )
            lastSpinNotificationTime = currentTime
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onCreate() {
        super.onCreate()

        createForegroundNotificationChannel()

        val notification = createForegroundNotification()
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        gyroscope?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(sensorEventListener)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createForegroundNotificationChannel() {
        val channel = NotificationChannel(
            FOREGROUND_CHANNEL_ID,
            "motion service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "detect motion in background"
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle("motion detection started")
//            .setContentText("monitoring")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        private const val FOREGROUND_CHANNEL_ID = "motion_detection_service_channel"
        private const val FOREGROUND_NOTIFICATION_ID = 1000
    }
}
