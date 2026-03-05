// asked chatgpt for small snippets, and approaches to working with graphql and json objects
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
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class CarPark(
    val name: String,
    val maxCapacity: Int? = null,
    val spacesAvailable: Int? = null
)

@Composable
fun ParkingSpacesScreen() {
    var kivisydan by remember { mutableStateOf<CarPark?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val client = remember { OkHttpClient() }
    val json = remember { Json { ignoreUnknownKeys = true } }

    LaunchedEffect(Unit) {
        // graphql example query from https://wp.oulunliikenne.fi/avoin-data/autoliikenne/graphql-rajapinnat/
        val queryText = """
            query GetAllCarParks {
              carParks {
                name
                maxCapacity
                spacesAvailable
              }
            }
        """.trimIndent()

        val requestBody = buildJsonObject {
            put("query", queryText)
        }.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://api.oulunliikenne.fi/proxy/graphql")
            .post(requestBody)
            .build()

        try {
            val body = withContext(Dispatchers.IO) {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) response.body.string() else null
                }
            }

            if (body != null) {
                val root = json.parseToJsonElement(body).jsonObject
                val carParks = root["data"]?.jsonObject?.get("carParks")?.jsonArray

                val match = carParks?.find {
                    it.jsonObject["name"]?.jsonPrimitive?.content == "Kivisydän"
                }

                if (match != null)
                    kivisydan = json.decodeFromJsonElement<CarPark>(match)

            }
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Kivisydän parking", style = MaterialTheme.typography.headlineMedium)

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (errorMessage != null) {
            Text(text = "Error: ${errorMessage!!}", modifier = Modifier.padding(top = 16.dp))
        } else if (kivisydan != null) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Free spaces: ${kivisydan!!.spacesAvailable ?: "Unknown"}")
                Text(text = "Max capacity: ${kivisydan!!.maxCapacity ?: "Unknown"}")
            }
        } else {
            Text(text = "Failed to get kivisydän info", modifier = Modifier.padding(top = 16.dp))
        }
    }
}
