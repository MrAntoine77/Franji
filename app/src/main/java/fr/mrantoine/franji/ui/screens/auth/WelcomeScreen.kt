package fr.mrantoine.franji.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.mrantoine.franji.R
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun WelcomeScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome_screen),
            contentDescription = "Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.xl, vertical = 128.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemedButton(
                text = "Créer un compte",
                onClick = {navController.navigate(Screen.Register.route)},
                isPrimary = true,
            )
            Spacer(modifier = Modifier.height(Dimens.m))

            ThemedButton(
                text = "Se connecter",
                onClick = {navController.navigate(Screen.Login.route)},
                isPrimary = false,
            )
            Spacer(modifier = Modifier.height(Dimens.m))
        }
    }
}