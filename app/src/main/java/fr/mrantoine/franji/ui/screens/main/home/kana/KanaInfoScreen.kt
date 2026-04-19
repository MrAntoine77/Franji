package fr.mrantoine.franji.ui.screens.main.home.kana

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Kana
import fr.mrantoine.franji.storage.KanaStorage
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.LottieStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.navigation.Tree
import fr.mrantoine.franji.ui.screens.main.cards.buildMap

data class Elt(
    val jp: String,
    val fr: String,
    val angles: List<Float>,
    val id: String
)


@Composable
fun KanaViewer(
    kana: Kana,
    modifier: Modifier = Modifier
) {
    var lectures = kana.lectures
    if (kana.lectures.isEmpty()) return

    var index by remember { mutableStateOf(0) }
    var lottie by remember { mutableStateOf("{}") }

    val current = lectures[index]
    val hasMultiple = lectures.size > 1

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (hasMultiple) {
                IconButton(
                    onClick = {
                        index = if (index - 1 < 0) lectures.lastIndex else index - 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            ClickableAnimatedText(
                text = current.fr,
                onClick = { TtsStorage.speak(current.jp) },
                fontSize = 24.sp
            )

            if (hasMultiple) {
                IconButton(
                    onClick = {
                        index = if (index + 1 > lectures.lastIndex) 0 else index + 1
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        val context = LocalContext.current
        LaunchedEffect(index) {
            lottie = LottieStorage.getLottieById(context,"${kana.main_id}_${index + 1}")
        }
        key(lottie) {
            Lottie(
                data = lottie,
                speed = 1.5f
            )
        }
    }
}

@Composable
fun KanaInfoScreen(
    navController: NavController,
    kanaId: String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 2,
                redirectRoute = Screen.SearchKana.route
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        var kana by remember { mutableStateOf(Kana()) }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            kana = KanaStorage.getKanaById(context, kanaId)
        }

        KanaViewer(
            kana = kana,
            modifier = Modifier.padding(innerPadding)
        )
    }
}