// waveform visualizer generated with gemini-3-flash-preview
package com.example.composetutorial.ui.theme.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import java.io.File
import java.io.IOException

@Composable
fun VoiceRecorderScreen() {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var audioFile by remember { mutableStateOf<File?>(null) }
    
    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }

    var amplitudes by remember { mutableStateOf(listOf<Float>()) }

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    // Polling for amplitude
    LaunchedEffect(isRecording) {
        if (isRecording) {
            amplitudes = emptyList()
            while (isRecording) {
                val amp = try {
                    recorder?.maxAmplitude?.toFloat() ?: 0f
                } catch (_: Exception) {
                    0f
                }
                // Normalize and add to list. maxAmplitude usually goes up to 32767.
                amplitudes = (amplitudes + (amp / 32767f)).takeLast(100)
                delay(100)
            }
        }
    }

    fun startRecording() {
        val file = File(context.cacheDir, "temp_recording.mp3")
        audioFile = file
        
        val newRecorder = MediaRecorder(context)
        
        newRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
            recorder = this
            isRecording = true
        }
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        isRecording = false
    }

    fun startPlaying() {
        val file = audioFile
        if (file == null || !file.exists()) return

        val newPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                start()
                isPlaying = true
                setOnCompletionListener {
                    isPlaying = false
                    release()
                    player = null
                }
            } catch (e: IOException) {
                Log.e("VoiceRecorder", "prepare() failed", e)
            }
        }
        player = newPlayer
    }

    fun stopPlaying() {
        player?.apply {
            stop()
            release()
        }
        player = null
        isPlaying = false
    }

    DisposableEffect(Unit) {
        onDispose {
            recorder?.release()
            player?.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!hasPermission) {
            Text("Microphone permission is required")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }) {
                Text("Grant permission")
            }
        } else {
            Text(
                text = if (isRecording) "Recording" else if (isPlaying) "Playing" else "Idle",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Waveform Visualizer
            WaveformVisualizer(
                amplitudes = amplitudes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Button(
                    onClick = { if (isRecording) stopRecording() else startRecording() },
                    enabled = !isPlaying
                ) {
                    Text(if (isRecording) "Stop Recording" else "Start Recording")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { if (isPlaying) stopPlaying() else startPlaying() },
                    enabled = !isRecording && audioFile != null && audioFile!!.exists()
                ) {
                    Text(if (isPlaying) "Stop Playback" else "Play Recording")
                }
            }
        }
    }
}

@Composable
fun WaveformVisualizer(amplitudes: List<Float>, modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primary
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2
        val count = amplitudes.size
        if (count > 0) {
            val spacing = width / 100 // Fixed max points for consistency
            amplitudes.forEachIndexed { index, amp ->
                val x = index * spacing
                // Make it look like a mirror waveform
                val barHeight = (amp * height).coerceAtLeast(4f) 
                drawLine(
                    color = color,
                    start = Offset(x, centerY - barHeight / 2),
                    end = Offset(x, centerY + barHeight / 2),
                    strokeWidth = spacing * 0.6f,
                    cap = StrokeCap.Round
                )
            }
        } else {
            // Draw a flat line when idle
            drawLine(
                color = color.copy(alpha = 0.3f),
                start = Offset(0f, centerY),
                end = Offset(width, centerY),
                strokeWidth = 2f
            )
        }
    }
}
