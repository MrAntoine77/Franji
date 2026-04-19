package fr.mrantoine.franji.ui.screens.main.home.kana

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.Kana
import fr.mrantoine.franji.storage.KanaStorage
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SearchKanaScreen(
    navController: NavController,
) {
    var search_text by rememberSaveable { mutableStateOf("") }
    var kanaList by remember { mutableStateOf<List<Kana>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 1,
                redirectRoute = Screen.SearchKana.route,
                onSearchChanged = { text ->
                    search_text = text
                },
                autoFocusSearch = if(search_text == "") true else false,
            )
        },
        bottomBar = {
            BottomBar(
                selectedIndex = 0,
                navController = navController
            )
        }
    ) { innerPadding ->

        LaunchedEffect(search_text) {
            loading = true
            delay(500)

            kanaList = if (search_text.isNotEmpty()) {
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    KanaStorage.search(search_text)

                }
            } else {
                emptyList()
            }

            loading = false
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(Dimens.m)
        ) {
            when {
                loading && search_text.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                kanaList.isEmpty() && search_text.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Aucun kana trouvée")
                    }
                }

                search_text.isNotEmpty() -> {
                    LazyColumn {
                        val columns = 5
                        val rows = (kanaList.size + columns - 1) / columns
                        for (rowIndex in 0 until rows) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (colIndex in 0 until columns) {
                                        val index = rowIndex * columns + colIndex
                                        if (index < kanaList.size) {
                                            if (kanaList[index].lectures.isNotEmpty()) {
                                                val kanaId = kanaList[index].main_id
                                                val kanaChar = kanaList[index].lectures[0].jp

                                                Box(
                                                    modifier = Modifier
                                                        .aspectRatio(1f)
                                                        .weight(1f)
                                                        .padding(Dimens.s)
                                                        .clickable {
                                                            navController.navigate(
                                                                Screen.KanaInfo.route(
                                                                    kanaId
                                                                )
                                                            )
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = kanaChar,
                                                        fontSize = 32.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .aspectRatio(1f)
                                                        .weight(1f)
                                                        .padding(Dimens.s),
                                                    contentAlignment = Alignment.Center
                                                ) {}
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
                else -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Rechercher un kana")
                    }
                }
            }
        }
    }
}