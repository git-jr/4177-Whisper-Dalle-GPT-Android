package com.alura.anotaai

import com.aallam.openai.api.audio.SpeechRequest
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.audio.Voice
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.image.ImageURL
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.alura.anotaai.media.FileUtils
import com.alura.anotaai.network.DownloadService
import okio.source
import java.io.File
import javax.inject.Inject

class OpenAIAPI @Inject constructor(
    private val fileUtils: FileUtils,
    private val downloadService: DownloadService,
) {
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
                    voice = Voice.Onyx,
                )
            )

            val (audioPath, audioDuration) = fileUtils.saveAudioInternalStorage(rawAudio)
            onResult(audioPath, audioDuration, summarizedText)
        }
    }

    suspend fun generateImage(
        prompt: String,
        onResult: (String) -> Unit
    ) {

        val images: List<ImageURL> = openAI.imageURL(
            creation = ImageCreation(
                prompt = prompt,
                model = ModelId("dall-e-3"),
                n = 1,
                size = ImageSize.is1024x1024
            )
        )

        downloadService.makeDownloadByURL(
            images.first().url,
            onSaved = {
               onResult(it)
            }
        )
    }


}