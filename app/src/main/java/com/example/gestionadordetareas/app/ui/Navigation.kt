package com.example.gestionadordetareas.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionadordetareas.ui.screens.EditarTareaScreen
import com.example.gestionadordetareas.ui.screens.ListaTareasScreen
import com.example.gestionadordetareas.viewmodel.TareaViewModel

sealed class Screen(val route: String) {
    object Lista : Screen("lista")
    object Editar : Screen("editar/{id}") {
        fun crearRuta(id: Int?) = "editar/${id ?: -1}"
    }
}

@Composable
fun AppNavGraph(viewModel: TareaViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route
    ) {
        composable(Screen.Lista.route) {
            ListaTareasScreen(
                viewModel = viewModel,
                onNuevaTarea = {
                    viewModel.resetFormulario()
                    navController.navigate(Screen.Editar.crearRuta(null))
                },
                onEditarTarea = { tarea ->
                    viewModel.cargarTareaParaEditar(tarea)
                    navController.navigate(Screen.Editar.crearRuta(tarea.id))
                }
            )
        }

        composable(
            Screen.Editar.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) {
            EditarTareaScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
