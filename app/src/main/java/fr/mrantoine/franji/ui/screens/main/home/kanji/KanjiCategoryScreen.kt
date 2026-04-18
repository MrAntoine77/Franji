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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun KanjiCategoryScreen(
    navController: NavController,
    categoryPath: String
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
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->

        var kanji_list_id by remember { mutableStateOf(emptyArray<String>()) }
        val kanji_list = remember { mutableStateListOf<Kanji>() }
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            kanji_list_id = CategoryStorage.getCategoryByPath(context, categoryPath)
            kanji_list_id.forEach { kanjiId ->
                kanji_list.add(KanjiStorage.getKanjiById(context, kanjiId))
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

                val columns = 5
                val rows = (kanji_list.size + columns - 1) / columns

                Column {
                    for (rowIndex in 0 until rows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (colIndex in 0 until columns) {
                                val index = rowIndex * columns + colIndex
                                if (index < kanji_list.size) {
                                    var kanjiId = kanji_list[index].id
                                    var kanjiChar = kanji_list[index].kanji

                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .weight(1f)
                                            .padding(Dimens.s)
                                            .clickable {
                                                navController.navigate(Screen.KanjiInfo.route(kanjiId))
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = kanjiChar,
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}