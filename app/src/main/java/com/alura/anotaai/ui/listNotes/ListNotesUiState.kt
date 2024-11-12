package com.alura.anotaai.ui.listNotes

import com.alura.anotaai.model.BaseNote
import com.alura.anotaai.model.NoteItemAudio

data class ListNotesUiState(
    val itemToDelete: BaseNote? = null,
    val showDialogTranscribeAudio: Boolean = false,
    val noteItemAudio: NoteItemAudio? = null
)