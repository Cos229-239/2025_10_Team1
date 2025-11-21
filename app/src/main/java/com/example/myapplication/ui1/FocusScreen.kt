package com.example.myapplication.ui1

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FocusScreen(navController: NavController) {
    val vm: FocusSessionViewModel = viewModel()
    FocusSessionScreen(vm = vm, navController = navController)
}
