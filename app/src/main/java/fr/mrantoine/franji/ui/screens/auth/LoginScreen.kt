package fr.mrantoine.franji.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.navigation.TopBar
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.UserStorage
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    BackHandler() {
        navController.navigate(Screen.Welcome.route)
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            showBack = true,
            title = "Se connecter",
            onBackClick = {navController.navigate(Screen.Welcome.route)}
        )
        Column(
            modifier = Modifier.padding(Dimens.m)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Adresse e-mail") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.s)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(Dimens.m))
            val scope = rememberCoroutineScope()
            ThemedButton(
                text = "Se connecter",
                onClick = {
                    scope.launch {
                        val response = UserStorage.login(email = email, password = password)
                        if (response.message == "Login successful") {
                            navController.navigate(Screen.HomeAll.route)
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            ThemedButton(
                text = "Créer un compte",
                onClick = {navController.navigate(Screen.Register.route)},
                isPrimary = false
            )

        }
    }
}
