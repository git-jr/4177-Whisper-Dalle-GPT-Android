package com.alura.anotaai.ui.listNotes

import androidx.lifecycle.ViewModel
import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.model.NoteItemAudio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ListNotesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ListNotesUiState())
    var uiState = _uiState.asStateFlow()

    fun showDialogTranscribeAudio(show: Boolean) {
        _uiState.value = _uiState.value.copy(showDialogTranscribeAudio = show)
    }

    fun setItemToDelete(item: BaseNote?) {
        _uiState.value = _uiState.value.copy(itemToDelete = item)
    }

    fun setNoteItemAudio(noteItemAudio: NoteItemAudio) {
        _uiState.value = _uiState.value.copy(noteItemAudio = noteItemAudio)
    }
}