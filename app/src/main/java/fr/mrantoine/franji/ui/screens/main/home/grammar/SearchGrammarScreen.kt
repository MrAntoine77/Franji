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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun SearchGrammarScreen(
    navController: NavController,
) {
    var search_text by remember { mutableStateOf("") }
    var grammarList by remember { mutableStateOf<List<Grammar>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                selectedCategoryIndex = 3,
                onSearchChanged = { text ->
                    search_text = text
                },
                autoFocusSearch = true,
                redirectRoute = Screen.SearchGrammar.route
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

        LaunchedEffect(search_text) {
            loading = true
            delay(500)

            grammarList = if (search_text.isNotEmpty()) {
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    GrammarStorage.search(search_text)

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

                grammarList.isEmpty() && search_text.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Aucune leçon de grammaire trouvée")
                    }
                }

                search_text.isNotEmpty() -> {
                    LazyColumn {

                        grammarList.forEach { grammar ->
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(Screen.GrammarInfo.route(grammar.id))
                                        }
                                        .padding(
                                            top = Dimens.s,
                                            bottom = Dimens.s
                                        ),
                                ) {
                                    Text(
                                        text = "${grammar.title} - ${grammar.subtitle}",
                                        fontSize = 16.sp,
                                        modifier = Modifier.weight(1f)
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
                else -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Rechercher une leçon de grammaire")
                    }
                }
            }
        }
    }
}