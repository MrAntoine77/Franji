package fr.mrantoine.franji.ui.screens.main.cards


import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.ui.components.navigation.EaseBar
import fr.mrantoine.franji.ui.components.navigation.TopBar
import fr.mrantoine.franji.ui.screens.main.cards.types.CardKanjiScreen
import fr.mrantoine.franji.ui.screens.main.cards.types.CardVocabScreen
import fr.mrantoine.franji.ui.theme.Dimens


@Composable
fun CardsPlayingScreen(
    navController: NavController,
    cardsArray: MutableList<String>,
    title: String
) {
    if(cardsArray.size > 0) {
        val isRevealed = remember { mutableStateOf(false) }
        var passed by remember { mutableStateOf(0) }
        var failed by remember { mutableStateOf(0) }
        var total by remember { mutableStateOf( cardsArray.size) }

        fun nextCard(ok: Boolean) {
            if (ok && cardsArray.size == 1) {
                navController.navigate(Screen.CardsList.route)
            } else {
                isRevealed.value = false
                val first = cardsArray.removeAt(0)
                if(ok) {
                    passed+=1
                }
                else {
                    if((passed + failed) < total) {
                        failed += 1
                    }
                    cardsArray.add(first)
                }
            }
        }

        val difficulties: Map<String, Pair<Color, () -> Unit>> = if (isRevealed.value) mapOf(
            "Revoir" to (Color(0xFFF44336) to { nextCard(false) }),
            "OK" to (Color(0xFF4CAF50) to { nextCard(true) })

        ) else mapOf(
            "Révéler" to (MaterialTheme.colorScheme.primary to { isRevealed.value = true })
        )

        Scaffold(
            topBar = {
                val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                TopBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = title,
                    showBack = true,
                    onBackClick = {backDispatcher?.onBackPressed()}
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
                modifier = Modifier.padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimens.s),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(passed.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Green
                    )
                    Text(failed.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Red
                    )
                    Text(cardsArray.size.toString(),
                        modifier = Modifier.padding(end = Dimens.s),
                        color = Color.Blue
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.s))

                if(cardsArray[0].startsWith("kanji")) {
                    CardKanjiScreen(
                        cardsArray = cardsArray,
                        isRevealed = isRevealed
                    )
                } else if(cardsArray[0].startsWith("vocab")) {
                    CardVocabScreen(
                        cardsArray = cardsArray,
                        isRevealed = isRevealed
                    )
                }
            }
        }
    }
}