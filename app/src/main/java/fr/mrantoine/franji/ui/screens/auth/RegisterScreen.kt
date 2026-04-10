package fr.mrantoine.franji.ui.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.navigation.TopBar
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.UserStorage
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var acceptTerms by remember { mutableStateOf(false) }
    var subscribeNewsletter by remember { mutableStateOf(false) }

    BackHandler() {
        navController.navigate(Screen.Welcome.route)
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            showBack = true,
            title = "Créer un compte",
            onBackClick = { navController.navigate(Screen.Welcome.route) }
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
                value = username,
                onValueChange = { username = it },
                label = { Text("Nom d'utilisateur") },
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = { acceptTerms = it }
                )
                Spacer(modifier = Modifier.width(Dimens.s))
                Text("J'accepte les conditions d'utilisation")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = subscribeNewsletter,
                    onCheckedChange = { subscribeNewsletter = it }
                )
                Spacer(modifier = Modifier.width(Dimens.s))
                Text("S'abonner à la newsletter")
            }
            Spacer(modifier = Modifier.height(Dimens.m))

            val scope = rememberCoroutineScope()
            var errorMessage by remember { mutableStateOf<String?>(null) }

            ThemedButton(
                text = "S'inscrire",
                onClick = {
                    scope.launch {
                        val response = UserStorage.register(username = username, email = email, password = password)
                        if (response.success) {
                            errorMessage = null
                            navController.navigate(Screen.Login.route)
                        } else {
                            errorMessage = response.message
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            ThemedButton(
                text = "Déja un compte ?",
                onClick = { navController.navigate(Screen.Login.route) },
                isPrimary = false
            )
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red
                )
            }
        }
    }
}
