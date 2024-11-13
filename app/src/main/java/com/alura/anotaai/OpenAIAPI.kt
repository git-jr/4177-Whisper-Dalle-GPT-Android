package com.alura.anotaai

import com.aallam.openai.api.audio.SpeechRequest
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.audio.Voice
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.alura.anotaai.media.FileUtils
import okio.source
import java.io.File
import javax.inject.Inject

class OpenAIAPI @Inject constructor(private val fileUtils: FileUtils) {
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

    suspend fun summarize(
        prompt: String,
        onResult: (String, Int, String) -> Unit
    ) {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-4o-mini"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            )
        )


        openAI.chatCompletion(chatCompletionRequest).let { response ->
            val summarizedText = response.choices.first().message.content.toString()
            val rawAudio: ByteArray = openAI.speech(
                request = SpeechRequest(
                    model = ModelId("tts-1"),
                    input = summarizedText,
                    voice = Voice.Nova,
                )
            )

            val (audioPath, audioDuration) = fileUtils.saveAudioInternalStorage(rawAudio)
            onResult(audioPath, audioDuration, summarizedText)
        }
    }
}