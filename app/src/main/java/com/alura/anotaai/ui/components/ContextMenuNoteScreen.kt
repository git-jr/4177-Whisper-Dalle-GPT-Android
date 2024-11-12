package com.alura.anotaai.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alura.anotaai.R
import com.alura.anotaai.ui.notes.NoteUiState

@Composable
fun ContextMenuNoteScreen(
    state: NoteUiState,
    onDismiss: () -> Unit,
    onSummarize: () -> Unit = {},
    onGenerateImage: () -> Unit = {}
) {
    DropdownMenu(
        expanded = state.showContextMenu,
        onDismissRequest = { onDismiss() },
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Resumir e ouvir")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        painterResource(R.drawable.ic_headphones),
                        contentDescription = "Resumir e ouvir",
                    )
                }
            },
            onClick = { onSummarize() }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Gerar capa")
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        painterResource(R.drawable.ic_wall_art),
                        contentDescription = "Gerar capa",
                    )
                }
            },
            onClick = { onGenerateImage() }
        )
    }
}
