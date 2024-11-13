package com.alura.anotaai

import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import okio.source
import java.io.File

class OpenAIAPI() {
    private val openAI by lazy { OpenAI(BuildConfig.OPENAIKEY) }

    suspend fun transcribeAudio(
        audioPath: String,
        onResult: (String) -> Unit
    ) {
        val audioSource = File(audioPath)

        val request = TranscriptionRequest(
            audio = FileSource(name = audioSource.name, source = audioSource.source()),
            model = ModelId("whisper-1"),
        )

        val transcription = openAI.transcription(request)
        val transcriptionText = transcription.text
        onResult(transcriptionText)
    }
}