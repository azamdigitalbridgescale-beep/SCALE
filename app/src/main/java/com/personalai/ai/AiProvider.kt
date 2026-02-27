package com.personalai.ai

data class AiRequest(
    val userText: String,
    val systemPrompt: String,
    val temperature: Double = 0.9
)

interface AiProvider {
    val name: String
    suspend fun generate(request: AiRequest): String
}
