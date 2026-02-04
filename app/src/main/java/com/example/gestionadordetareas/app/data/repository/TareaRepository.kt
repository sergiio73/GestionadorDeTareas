package com.example.gestionadordetareas.data.repository

import com.example.gestionadordetareas.data.local.dao.TareaDao
import com.example.gestionadordetareas.data.local.model.Tarea
import kotlinx.coroutines.flow.Flow

class TareaRepository(
    private val tareaDao: TareaDao
) {
    fun obtenerTodas(): Flow<List<Tarea>> = tareaDao.obtenerTodas()

    suspend fun obtenerPorId(id: Int): Tarea? = tareaDao.obtenerPorId(id)

    suspend fun insertar(tarea: Tarea) = tareaDao.insertar(tarea)

    suspend fun actualizar(tarea: Tarea) = tareaDao.actualizar(tarea)

    suspend fun eliminar(tarea: Tarea) = tareaDao.eliminar(tarea)
}
