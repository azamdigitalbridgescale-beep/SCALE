package com.personalai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.personalai.ai.CreativeOrchestrator
import com.personalai.ai.GeminiProvider
import com.personalai.ai.OpenAiProvider
import com.personalai.voice.VoiceAssistantManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var voiceManager: VoiceAssistantManager? = null

    private val requestMicPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) voiceManager?.startListening()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val openAi = OpenAiProvider(apiKey = BuildConfig.OPENAI_API_KEY)
        val gemini = GeminiProvider(apiKey = BuildConfig.GEMINI_API_KEY)
        val orchestrator = CreativeOrchestrator(gemini = gemini, openAi = openAi)

        setContent {
            var prompt by remember { mutableStateOf("") }
            var response by remember { mutableStateOf("Ask me anything.") }

            voiceManager = VoiceAssistantManager(this) {
                prompt = it
            }

            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text("Speak or type your request") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            lifecycleScope.launch {
                                response = "Thinking..."
                                response = orchestrator.generateCreativeAssistantReply(prompt)
                            }
                        }) {
                            Text("Ask AI")
                        }

                        Button(onClick = { ensureMicPermissionAndListen() }) {
                            Text("🎤 Voice")
                        }
                    }

                    Text(text = response)
                }
            }
        }
    }

    private fun ensureMicPermissionAndListen() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                voiceManager?.startListening()
            }
            else -> requestMicPermission.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    override fun onDestroy() {
        voiceManager?.destroy()
        super.onDestroy()
    }
}
