package com.personalai.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class OpenAiProvider(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) : AiProvider {

    override val name: String = "openai"

    override suspend fun generate(request: AiRequest): String = withContext(Dispatchers.IO) {
        val payload = """
            {
              "model": "gpt-4o-mini",
              "messages": [
                {"role":"system","content":"${request.systemPrompt}"},
                {"role":"user","content":"${request.userText}"}
              ],
              "temperature": ${request.temperature}
            }
        """.trimIndent()

        val httpRequest = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(payload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(httpRequest).execute().use { response ->
            if (!response.isSuccessful) {
                return@withContext "OpenAI error: ${response.code}"
            }
            response.body?.string() ?: "OpenAI empty response"
        }
    }
}
