package com.alura.anotaai

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.alura.anotaai.ui.navigation.NavHost
import com.alura.anotaai.ui.theme.AnotaAITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val openAI = OpenAI(BuildConfig.OPENAIKEY)

//        val chatCompletionRequest = ChatCompletionRequest(
//            model = ModelId("gpt-4o-mini"),
//            messages = listOf(
//                ChatMessage(
//                    role = ChatRole.User,
//                    content = "Qual o nome do Android 9"
//                )
//            )
//        )
//        lifecycleScope.launch {
//            val message: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
//            val response = message.choices.first().message.content.toString()
//            Log.d("message", response)
//        }

        setContent {
            AnotaAITheme {
                NavHost(rememberNavController(),
                    startRecording = { startRecording(it) },
                    stopRecording = { stopRecording() },
                    startPlaying = { startPlaying(it) },
                    stopPlaying = { stopPlaying() }
                )
            }
        }
    }

    private fun startRecording(audioPath: String) {
        val context = this
        val mediaRecorder: MediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }
        recorder = mediaRecorder
            .apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(audioPath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)

                try {
                    prepare()
                    Toast.makeText(context, "Gravando", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(context, "Erro ao iniciar gravação $e", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("LOG_TAG", "prepare() failed $e")
                }

                start()
            }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun startPlaying(fileName: String) {
        val context = this
        stopPlaying()

        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
                Toast.makeText(context, "Reproduzindo", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Erro ao iniciar reprodução", Toast.LENGTH_SHORT)
                    .show()
                Log.e("LOG_TAG", "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
    }
}
