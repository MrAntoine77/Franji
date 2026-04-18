package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun VocabCategoryScreen(
    navController: NavController,
    categoryPath: String
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 3
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->

        var vocab_list_id by remember { mutableStateOf(emptyArray<String>()) }
        val vocab_list = remember { mutableStateListOf<Vocab>() }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            vocab_list_id = CategoryStorage.getCategoryByPath(context, categoryPath)
            vocab_list_id.forEach { vocabId ->
                vocab_list.add(VocabStorage.getVocabById(context, vocabId))
            }
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            var title = categoryPath.uppercase().replace("/", " ")
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.m)
            ) {
                Text(
                    text = "- $title -",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.m),
                    textAlign = TextAlign.Center
                )

                Column {
                    vocab_list.forEach { vocab ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Screen.VocabInfo.route(vocab.id))
                                }
                                .padding(
                                    top=Dimens.s,
                                    bottom=Dimens.s
                                ),
                        ) {
                            Text(
                                text = vocab.jp,
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = vocab.fr,
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.tertiary,
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}