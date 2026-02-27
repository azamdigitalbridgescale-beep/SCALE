# Personal Android AI (Gemini + ChatGPT + Assistant + Siri style)

This repo now includes a starter Android app architecture for a **personal voice-first AI assistant** with:

- **Multi-model reasoning** (Gemini + OpenAI + optional local model)
- **Creativity mode** (idea generation + style blending)
- **Assistant actions** (alarms, apps, web search, reminders stubs)
- **On-device wake and voice pipeline scaffolding**

> This is a production-oriented starter: you can run and extend it, but you still need API keys and real integrations.

## What you get

- Kotlin + Jetpack Compose UI
- Provider abstraction for multiple LLMs
- Routing/orchestration layer that chooses the best provider per task
- Action execution interfaces for Android capabilities
- Secure key strategy using `BuildConfig`

## Project structure

```
app/
  src/main/java/com/personalai/
    MainActivity.kt
    ai/
      AiProvider.kt
      OpenAiProvider.kt
      GeminiProvider.kt
      CreativeOrchestrator.kt
    voice/
      VoiceAssistantManager.kt
    actions/
      DeviceActionExecutor.kt
```

## Build steps

1. Create a new Android Studio project (Empty Compose Activity).
2. Copy these files into the matching paths.
3. Add these dependencies in `app/build.gradle.kts`:
   - `implementation("com.squareup.okhttp3:okhttp:4.12.0")`
   - `implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")`
   - Compose dependencies from your template.
4. Add in `local.properties`:
   - `OPENAI_API_KEY=your_key`
   - `GEMINI_API_KEY=your_key`
5. Expose keys through `build.gradle.kts` `buildConfigField` entries.
6. Run on a real device and grant microphone permission.

## How the “Creativity of Gemini + ChatGPT + Assistant + Siri” is handled

- **Gemini**: broad ideation and multimodal-friendly prompting.
- **ChatGPT(OpenAI)**: strong coding/writing reasoning.
- **Assistant/Siri style**: intent parsing + device action execution layer.
- **Blended creativity**: orchestrator requests two responses, merges them into a final answer with a selectable creative temperature profile.

## Next upgrades

- Add wake-word engine (Porcupine or Android hotword APIs where available).
- Add persistent memory with Room.
- Add RAG with private notes/files.
- Add on-device fallback model (MediaPipe / llama.cpp Android binding).
- Add function-calling JSON schema for safe action routing.
