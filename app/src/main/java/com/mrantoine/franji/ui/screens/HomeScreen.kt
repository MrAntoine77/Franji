package com.mrantoine.franji.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mrantoine.franji.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    // Centrer et limiter la taille du bouton
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate(Screen.Quiz.route) },
            modifier = Modifier.widthIn(min = 100.dp, max = 200.dp) // largeur limitée
        ) {
            Text("Aller au quiz")
        }
    }
}
