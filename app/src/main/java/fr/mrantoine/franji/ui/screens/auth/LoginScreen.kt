package fr.mrantoine.franji.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.TopBar
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.text.input.PasswordVisualTransformation
import fr.mrantoine.franji.ui.components.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun LoginScreen(
    onBackClick: (() -> Unit) = {},
    onLoginClick: (() -> Unit) = {},
    onRegisterClick: (() -> Unit) = {}

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            showBack = true,
            title = "Se connecter",
            onBackClick = onBackClick
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

            ThemedButton(
                text = "Se connecter",
                onClick = onLoginClick
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            ThemedButton(
                text = "Créer un compte",
                onClick = onRegisterClick,
                isPrimary = false
            )

        }
    }
}
