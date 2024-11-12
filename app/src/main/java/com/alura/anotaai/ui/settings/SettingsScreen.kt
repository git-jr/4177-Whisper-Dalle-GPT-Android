package com.alura.anotaai.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.anotaai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    title = {
                        Text(
                            text = "Configurações",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    },
                )
                HorizontalDivider()
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = paddingValues.calculateBottomPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.size(50.dp))
                AsyncImage(
                    R.mipmap.ic_launcher_foreground,
                    contentDescription = "Logo do app",
                    modifier = Modifier.size(300.dp),
                )
                Text(
                    text = stringResource(R.string.aqui_voce_gerencia),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.size(50.dp))

                Text(
                    text = stringResource(R.string.notas_cadastradas, state.notesCount),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )

                Button(
                    onClick = { viewModel.showDeleteDialog(true) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.apagar_todas_as_notas))
                }
            }

            if (state.showConfirmDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.showDeleteDialog(false) },
                    title = { Text(text = "Apagar todas notas") },
                    text = { Text(stringResource(R.string.tem_certeza_que_deseja_excluir_todas_as_notas)) },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.showDeleteDialog(false)
                                viewModel.deleteAllNotes()
                            }
                        ) {
                            Text("Sim")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { viewModel.showDeleteDialog(false) }
                        ) {
                            Text("Não")
                        }
                    }
                )
            }
        }
    )
}
