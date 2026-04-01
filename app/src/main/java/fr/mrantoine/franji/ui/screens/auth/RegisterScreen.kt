package fr.mrantoine.franji.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.mrantoine.franji.ui.components.TopBar
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import fr.mrantoine.franji.ui.components.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun RegisterScreen(
    onBackClick: (() -> Unit) = {},
    onRegisterClick: (() -> Unit) = {},
    onLoginClick: (() -> Unit) = {}

) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var acceptTerms by remember { mutableStateOf(false) }
    var subscribeNewsletter by remember { mutableStateOf(false) }


    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            showBack = true,
            title = "Créer un compte",
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

            ThemedButton(
                text = "S'inscrire",
                onClick = onRegisterClick
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            ThemedButton(
                text = "Déja un compte ?",
                onClick = onLoginClick,
                isPrimary = false
            )
        }
    }
}
