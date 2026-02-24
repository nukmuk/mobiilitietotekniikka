package com.example.composetutorial.ui.theme.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

// code borrowed from https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#kotlin and adapted to compose by claude sonnet 4.5

@Composable
fun SensorsScreen() {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // State to hold sensor values
    var luxValue by remember { mutableFloatStateOf(0f) }
    var sensorAccuracy by remember { mutableIntStateOf(0) }

    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }
    var rotationZ by remember { mutableFloatStateOf(0f) }
    var rotationAmount by remember { mutableFloatStateOf(0f) }

    // DisposableEffect to handle sensor lifecycle
    DisposableEffect(Unit) {
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_LIGHT -> {
                        luxValue = event.values[0]
                    }
                    Sensor.TYPE_GYROSCOPE -> {
                        rotationX = event.values[0]
                        rotationY = event.values[1]
                        rotationZ = event.values[2]
                        rotationAmount = sqrt(rotationX + rotationY + rotationZ )
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Do something here if sensor accuracy changes.
                sensorAccuracy = accuracy
            }
        }

        // Register the listeners
        lightSensor?.also { light ->
            sensorManager.registerListener(
                sensorEventListener,
                light,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        gyroscope?.also { gyro ->
            sensorManager.registerListener(
                sensorEventListener,
                gyro,
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
        Text(text = "Sensor Demo")
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Light Sensor")
        Text(text = "Light Level: $luxValue lux")
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Gyroscope (Spinning Detection)")
        Text(text = "Rotation X: %.2f rad/s".format(rotationX))
        Text(text = "Rotation Y: %.2f rad/s".format(rotationY))
        Text(text = "Rotation Z: %.2f rad/s".format(rotationZ))
        Text(text = "Magnitude: %.2f rad/s".format(rotationAmount))

        if (rotationAmount > 2.0f) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "⚠️ SPINNING DETECTED!", color = androidx.compose.ui.graphics.Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sensor Accuracy: $sensorAccuracy")
    }
}