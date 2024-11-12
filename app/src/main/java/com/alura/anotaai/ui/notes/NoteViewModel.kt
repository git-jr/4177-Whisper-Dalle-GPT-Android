package com.alura.anotaai.ui.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.image.ImageURL
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.alura.anotaai.BuildConfig
import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import com.alura.anotaai.network.DownloadService
import com.alura.anotaai.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.source
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val downloadService: DownloadService,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteUiState())
    var uiState = _uiState.asStateFlow()

    fun getNoteById(noteId: String) {
        viewModelScope.launch {
            noteRepository.getNoteById(noteId)?.let {
                _uiState.value = NoteUiState(note = it, noteTextAppBar = it.title)
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            noteRepository.addNote(_uiState.value.note.copy(title = _uiState.value.noteTextAppBar))
        }
    }

    fun deleteItemNote(noteItem: BaseNote) {
        viewModelScope.launch {
            noteRepository.removeItemNote(noteItem)
            updateCurrentNote()
        }
    }

    private fun updateCurrentNote() {
        viewModelScope.launch {
            _uiState.value.note.id.let {
                getNoteById(it)
            }
        }
    }

    fun updateNoteTextAppBar(text: String) {
        _uiState.value = _uiState.value.copy(noteTextAppBar = text)
    }

    fun addNewItemImage(imageLink: String) {
        val listItems = _uiState.value.note.listItems.toMutableList()
        listItems.add(NoteItemImage(link = imageLink, date = System.currentTimeMillis()))
        _uiState.value = _uiState.value.copy(note = _uiState.value.note.copy(listItems = listItems))
    }

    fun addNewItemAudio() {
        val listItems = _uiState.value.note.listItems.toMutableList()
        listItems.add(
            NoteItemAudio(
                link = _uiState.value.audioPath,
                duration = _uiState.value.audioDuration,
                date = System.currentTimeMillis(),
                transcription = _uiState.value.audioTranscription
            )
        )
        _uiState.value = _uiState.value.copy(
            note = _uiState.value.note.copy(listItems = listItems),
            addAudioNote = true
        )
    }

    fun addNewItemText() {
        val listItems = _uiState.value.note.listItems.toMutableList()
        listItems.add(
            NoteItemText(
                content = _uiState.value.noteText,
                date = System.currentTimeMillis()
            )
        )
        _uiState.value = _uiState.value.copy(
            note = _uiState.value.note.copy(listItems = listItems),
            noteText = ""
        )
    }

    fun updateItemText(newText: String, id: String) {
        val updatedList = _uiState.value.note.listItems.map { item ->
            if (item.id == id && item is NoteItemText) item.copy(content = newText) else item
        }
        _uiState.value =
            _uiState.value.copy(note = _uiState.value.note.copy(listItems = updatedList))
    }

    fun updateNoteText(text: String) {
        _uiState.value = _uiState.value.copy(noteText = text)
    }

    fun updateShowCameraState(show: Boolean) {
        _uiState.value = _uiState.value.copy(showCameraScreen = show)
    }

    fun updateIsRecording(recording: Boolean) {
        _uiState.value = _uiState.value.copy(isRecording = recording)
    }

    fun updateAddAudioNote(add: Boolean) {
        _uiState.value = _uiState.value.copy(addAudioNote = add)
    }

    fun updateAudioDuration(newDuration: Int) {
        _uiState.value = _uiState.value.copy(audioDuration = newDuration)
    }

    fun setAudioPath(audioPath: String) {
        _uiState.value = _uiState.value.copy(audioPath = audioPath)
    }

    fun showContextMenu(show: Boolean) {
        _uiState.value = _uiState.value.copy(showContextMenu = show)
    }

    private fun showLoading(show: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = show)
    }

    private fun setAudioProperties(audioPath: String, duration: Int, transcription: String) {
        _uiState.value = _uiState.value.copy(
            audioDuration = duration,
            audioTranscription = transcription,
            audioPath = audioPath
        )
    }

    private fun updateAudioTranscription(transcribedText: String, id: String) {
        _uiState.value = _uiState.value.copy(
            note = _uiState.value.note.copy(
                listItems = _uiState.value.note.listItems.map { item ->
                    if (item.id == id && item is NoteItemAudio) {
                        item.copy(transcription = transcribedText)
                    } else {
                        item
                    }
                }
            )
        )
    }

    fun transcribeAudio(noteItemAudio: NoteItemAudio) {
        showLoading(true)
        val openAI = OpenAI(BuildConfig.OPENAIKEY)

        val audioSource = File(noteItemAudio.link)

        val request = TranscriptionRequest(
            audio = FileSource(name = audioSource.name, source = audioSource.source()),
            model = ModelId("whisper-1"),
        )
        viewModelScope.launch {
            val transcription = openAI.transcription(request)
            val transcriptionText = transcription.text
            Log.d("transcription", transcriptionText)
            updateAudioTranscription(transcriptionText,noteItemAudio.id)
            showLoading(false)
        }
    }

    fun generateImage(){
        val openAI = OpenAI(BuildConfig.OPENAIKEY)

        val textNotes: List<String> = _uiState.value.note.listItems.filterIsInstance<NoteItemText>().map { it.content }
        val audioNotes: List<String> = _uiState.value.note.listItems.filterIsInstance<NoteItemAudio>().map { it.transcription }
        val title = _uiState.value.note.title
        val listItems = textNotes + audioNotes

        val prompt = "Crie uma imagem que respresente visualmente essa nota de $title com os itens: $listItems"

        viewModelScope.launch {
            val images: List<ImageURL> = openAI.imageURL(
                creation = ImageCreation(
                    prompt = "Um urso programador",
                    model = ModelId("dall-e-3"),
                    n = 1,
                    size = ImageSize.is1024x1024
                )
            )

            images.first().url.let {
                Log.d("imageAI", it)
            }
        }
    }


}