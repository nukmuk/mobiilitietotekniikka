package com.example.composetutorial.ui.theme.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// code borrowed from https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#kotlin and adapted to compose by claude sonnet 4.5

@Composable
fun SensorsScreen() {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // State to hold sensor values
    var luxValue by remember { mutableStateOf(0f) }
    var sensorAccuracy by remember { mutableStateOf(0) }

    // DisposableEffect to handle sensor lifecycle
    DisposableEffect(Unit) {
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // The light sensor returns a single value.
                // Many sensors return 3 values, one for each axis.
                luxValue = event.values[0]
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Do something here if sensor accuracy changes.
                sensorAccuracy = accuracy
            }
        }

        // Register the listener
        lightSensor?.also { light ->
            sensorManager.registerListener(
                sensorEventListener,
                light,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        // Cleanup when the composable leaves the composition
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Light Sensor Demo")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Light Level: $luxValue lux")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Sensor Accuracy: $sensorAccuracy")
    }
}