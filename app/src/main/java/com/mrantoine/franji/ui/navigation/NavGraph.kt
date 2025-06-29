package com.mrantoine.franji.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mrantoine.franji.ui.screens.HomeScreen
import com.mrantoine.franji.ui.screens.QuizScreen
import com.mrantoine.franji.ui.screens.SettingsScreen

@Composable
fun FranjiNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Quiz.route) {
            QuizScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}