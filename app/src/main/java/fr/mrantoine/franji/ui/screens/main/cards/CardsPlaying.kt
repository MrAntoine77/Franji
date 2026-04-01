package fr.mrantoine.franji.ui.screens.main.cards


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.ui.components.EaseBar
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.TopBar
import fr.mrantoine.franji.ui.theme.Dimens


@Composable
fun CardsPlaying(
    title: String = "Titre"
) {
    Column(
        modifier = Modifier.statusBarsPadding().padding(Dimens.s).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }
    val isRevealed = remember { mutableStateOf(false) }


    val difficulties: Map<String, Pair<Color, () -> Unit>> = if (isRevealed.value) mapOf(
        "Facile\n5min" to (Color(0xFF4CAF50) to { isRevealed.value = false }),
        "Normal\n10min" to (Color(0xFFFFC107) to { isRevealed.value = false }),
        "Difficile\n30min" to (Color(0xFFF44336) to { isRevealed.value = false }),
        "À revoir\n1h" to (Color(0xFF2196F3) to { isRevealed.value = false }),
        "Terminé\n4j" to (Color(0xFF9C27B0) to { isRevealed.value = false })
    ) else mapOf(
        "Révéler" to (MaterialTheme.colorScheme.primary to { isRevealed.value = true })
    )


    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = title,
                showBack = true
            )
        },
        bottomBar = {
            EaseBar(
                modifier = Modifier.navigationBarsPadding(),
                items = difficulties
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("10",
                    modifier = Modifier.padding(end = Dimens.s),
                    color = Color.Green
                )
                Text("10",
                    modifier = Modifier.padding(end = Dimens.s),
                    color = Color.Red
                )
                Text("10",
                    modifier = Modifier.padding(end = Dimens.s),
                    color = Color.Blue
                )
            }

            Spacer(modifier = Modifier.height(Dimens.l))

            Text(
                text = "clair",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            if(isRevealed.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "aka(rui)",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimens.l),
                        textAlign = TextAlign.Center
                    )
                    Lottie()
                }
            }
        }

    }

}