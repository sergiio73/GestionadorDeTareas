// app/viewmodel/TareaViewModel.kt
package com.example.gestionadordetareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionadordetareas.data.local.model.Tarea
import com.example.gestionadordetareas.data.repository.TareaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TareaFormState(
    val titulo: String = "",
    val descripcion: String = "",
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val estaGuardando: Boolean = false,
    val idEditando: Int? = null
)

class TareaViewModel(
    private val repository: TareaRepository
) : ViewModel() {

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    private val _formState = MutableStateFlow(TareaFormState())
    val formState: StateFlow<TareaFormState> = _formState.asStateFlow()

    init {
        // Cargar tareas de Room
        viewModelScope.launch(Dispatchers.IO) {
            repository.obtenerTodas().collect { lista ->
                _tareas.value = lista
            }
        }
    }

    fun onTituloChange(nuevo: String) {
        _formState.update {
            it.copy(
                titulo = nuevo,
                tituloError = validarTitulo(nuevo)
            )
        }
    }

    fun onDescripcionChange(nuevo: String) {
        _formState.update {
            it.copy(
                descripcion = nuevo,
                descripcionError = validarDescripcion(nuevo)
            )
        }
    }

    private fun validarTitulo(titulo: String): String? {
        return when {
            titulo.isBlank() -> "El título es obligatorio"
            titulo.length < 3 -> "Mínimo 3 caracteres"
            else -> null
        }
    }

    private fun validarDescripcion(desc: String): String? {
        return if (desc.length > 200) "Máximo 200 caracteres" else null
    }

    private fun formularioEsValido(state: TareaFormState): Boolean {
        return state.tituloError == null &&
                state.descripcionError == null &&
                state.titulo.isNotBlank()
    }

    fun resetFormulario() {
        _formState.value = TareaFormState()
    }

    fun cargarTareaParaEditar(tarea: Tarea) {
        _formState.value = TareaFormState(
            titulo = tarea.titulo,
            descripcion = tarea.descripcion,
            idEditando = tarea.id
        )
    }

    fun guardarTarea() {
        val state = _formState.value
        val tituloError = validarTitulo(state.titulo)
        val descError = validarDescripcion(state.descripcion)

        _formState.value = state.copy(
            tituloError = tituloError,
            descripcionError = descError
        )

        if (!formularioEsValido(_formState.value)) return

        viewModelScope.launch(Dispatchers.IO) {
            _formState.update { it.copy(estaGuardando = true) }

            if (state.idEditando == null) {
                repository.insertar(
                    Tarea(
                        titulo = state.titulo,
                        descripcion = state.descripcion
                    )
                )
            } else {
                repository.actualizar(
                    Tarea(
                        id = state.idEditando,
                        titulo = state.titulo,
                        descripcion = state.descripcion
                    )
                )
            }

            _formState.value = TareaFormState()
        }
    }

    fun marcarCompletada(tarea: Tarea, completada: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.actualizar(tarea.copy(esCompletada = completada))
        }
    }

    fun eliminarTarea(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.eliminar(tarea)
        }
    }
}
