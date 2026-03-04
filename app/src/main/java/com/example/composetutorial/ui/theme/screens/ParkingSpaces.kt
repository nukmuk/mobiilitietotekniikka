package com.example.composetutorial.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class CarPark(
    val carParkId: String,
    val name: String,
    val maxCapacity: Int?,
    val spacesAvailable: Int?
)

@Serializable
data class CarParkData(
    val carParks: List<CarPark>
)

@Serializable
data class GraphQLResponse(
    val data: CarParkData
)

@Composable
fun ParkingSpacesScreen() {
    var kivisydan by remember { mutableStateOf<CarPark?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val client = remember { OkHttpClient() }
    val json = remember { Json { ignoreUnknownKeys = true } }

    LaunchedEffect(Unit) {
        val query = """
            query GetAllCarParks {
              carParks {
                carParkId
                name
                lat
                lon
                maxCapacity
                spacesAvailable
              }
            }
        """.trimIndent()

        val requestBody = "{\"query\": \"${query.replace("\n", "\\n").replace("\"", "\\\"")}\"}"
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.oulunliikenne.fi/proxy/graphql")
            .post(requestBody)
            .build()

        try {
            val body = withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.string()
                    } else {
                        errorMessage = "Error: ${response.code}"
                        null
                    }
                }
            }

            if (body != null) {
                val graphQLResponse = json.decodeFromString<GraphQLResponse>(body)
                kivisydan = graphQLResponse.data.carParks.find { it.name == "Kivisydän" }
            }
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
        } else if (kivisydan != null) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Name: ${kivisydan!!.name}")
                Text(text = "Available: ${kivisydan!!.spacesAvailable ?: "N/A"}")
                Text(text = "Max Capacity: ${kivisydan!!.maxCapacity ?: "N/A"}")
            }
        } else {
            Text(text = "Kivisydän not found", modifier = Modifier.padding(top = 16.dp))
        }
    }
}
