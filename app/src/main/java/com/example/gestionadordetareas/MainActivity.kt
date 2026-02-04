package com.example.gestionadordetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gestionadordetareas.data.local.database.AppDatabase
import com.example.gestionadordetareas.data.repository.TareaRepository
import com.example.gestionadordetareas.ui.AppNavGraph
import com.example.gestionadordetareas.ui.theme.GestionadorDeTareasTheme
import com.example.gestionadordetareas.viewmodel.TareaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val db = AppDatabase.getInstance(applicationContext)
        val repository = TareaRepository(db.tareaDao())

        val viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return TareaViewModel(repository) as T
                }
            }
        )[TareaViewModel::class.java]

        setContent {
            GestionadorDeTareasTheme {
                AppNavGraph(viewModel = viewModel)
            }
        }
    }
}
