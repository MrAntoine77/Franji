package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SearchVocabScreen(
    navController: NavController
) {
    var search_text by rememberSaveable { mutableStateOf("") }
    var vocabList by remember { mutableStateOf<List<Vocab>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopBar(
                text = search_text,
                navController = navController,
                selectedCategoryIndex = 2,
                onSearchChanged = { text ->
                    search_text = text
                },
                autoFocusSearch = if(search_text == "") true else false,
                redirectRoute = Screen.SearchVocab.route
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

            vocabList = if (search_text.isNotEmpty()) {
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    VocabStorage.search(search_text)

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

                vocabList.isEmpty() && search_text.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Aucun mot trouvé")
                    }
                }

                search_text.isNotEmpty() -> {
                    LazyColumn {
                        vocabList.forEach { vocab ->
                            item {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navController.navigate(Screen.VocabInfo.route(vocab.id))
                                            }
                                            .padding(vertical = Dimens.s)
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

                else -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Rechercher un mot")
                    }
                }
            }
        }
    }
}