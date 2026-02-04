package com.example.gestionadordetareas.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestionadordetareas.viewmodel.TareaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarTareaScreen(
    viewModel: TareaViewModel,
    onBack: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (formState.idEditando == null) "Nueva tarea" else "Editar tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )

                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .animateContentSize(animationSpec = spring())
        ) {
            OutlinedTextField(
                value = formState.titulo,
                onValueChange = { viewModel.onTituloChange(it) },
                label = { Text("Título") },
                isError = formState.tituloError != null,
                supportingText = {
                    formState.tituloError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = formState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción") },
                isError = formState.descripcionError != null,
                supportingText = {
                    formState.descripcionError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            val hayErrores = formState.tituloError != null ||
                    formState.descripcionError != null ||
                    formState.titulo.isBlank()

            Button(
                onClick = {
                    viewModel.guardarTarea()
                    if (!hayErrores) {
                        onBack()
                    }
                },
                enabled = !hayErrores && !formState.estaGuardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (formState.idEditando == null) "Guardar" else "Actualizar")
            }
        }
    }
}
