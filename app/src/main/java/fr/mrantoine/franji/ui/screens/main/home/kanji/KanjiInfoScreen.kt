package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.components.Lecture
import fr.mrantoine.franji.ui.components.Lottie
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun KanjiInfoScreen(
    navController: NavController,
    kanjiId: String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 1
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.navigationBarsPadding(),
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->
        val scrollState = rememberLazyListState()
        var kanji by remember { mutableStateOf(Kanji()) }
        var lottie by remember { mutableStateOf("{}") }


        LaunchedEffect(Unit) {
            kanji = KanjiStorage.getKanjiById(kanjiId)
            kanji.let {
                lottie = KanjiStorage.getLottieByKanjiId(it.id)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(Dimens.m)
        ) {

            item {
                Lottie(
                    data = lottie,
                    speed = 2f
                )
            }
            kanji.lectures.forEach { lecture ->
                item {
                    Lecture(lecture)
                }
            }
            if(kanji.vocab.size > 0)
            {
                item {
                    Text(
                        text = "Utilisations :",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = Dimens.m),
                        textAlign = TextAlign.Start
                    )
                }
                kanji.vocab.forEach { vocabId ->
                    item {
                        Column() {
                            var vocab by remember { mutableStateOf(Vocab()) }

                            LaunchedEffect(Unit) {
                                vocab = VocabStorage.getVocabById(vocabId)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate(Screen.VocabInfo.route(vocabId)) }
                                    .padding(vertical = Dimens.s, horizontal = Dimens.m),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = vocab.jp,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = vocab.fr,
                                    fontSize = 20.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.End
                                )
                            }
                            HorizontalDivider(
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}