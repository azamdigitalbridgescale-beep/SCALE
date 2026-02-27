package com.personalai.ai

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CreativeOrchestrator(
    private val gemini: AiProvider,
    private val openAi: AiProvider
) {
    suspend fun generateCreativeAssistantReply(userText: String): String = coroutineScope {
        val systemPrompt = """
            You are a personal AI assistant for Android.
            Blend creativity, practical help, and concise action suggestions.
            If an Android action is needed, include an ACTION_HINT in one line.
        """.trimIndent()

        val geminiResponse = async {
            gemini.generate(
                AiRequest(
                    userText = userText,
                    systemPrompt = systemPrompt,
                    temperature = 1.0
                )
            )
        }

        val openAiResponse = async {
            openAi.generate(
                AiRequest(
                    userText = userText,
                    systemPrompt = systemPrompt,
                    temperature = 0.8
                )
            )
        }

        val mergedPrompt = """
            Merge the following two assistant drafts into one final answer.
            Keep it natural, useful, and energetic.

            [Gemini Draft]
            ${geminiResponse.await()}

            [OpenAI Draft]
            ${openAiResponse.await()}
        """.trimIndent()

        openAi.generate(
            AiRequest(
                userText = mergedPrompt,
                systemPrompt = "You are the final response composer.",
                temperature = 0.9
            )
        )
    }
}
