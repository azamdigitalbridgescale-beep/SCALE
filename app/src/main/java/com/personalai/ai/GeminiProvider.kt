package com.personalai.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class GeminiProvider(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) : AiProvider {

    override val name: String = "gemini"

    override suspend fun generate(request: AiRequest): String = withContext(Dispatchers.IO) {
        val payload = """
            {
              "contents": [
                {
                  "parts": [
                    {"text":"${request.systemPrompt}\n\nUser: ${request.userText}"}
                  ]
                }
              ],
              "generationConfig": {
                "temperature": ${request.temperature}
              }
            }
        """.trimIndent()

        val httpRequest = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
            .addHeader("Content-Type", "application/json")
            .post(payload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(httpRequest).execute().use { response ->
            if (!response.isSuccessful) {
                return@withContext "Gemini error: ${response.code}"
            }
            response.body?.string() ?: "Gemini empty response"
        }
    }
}
