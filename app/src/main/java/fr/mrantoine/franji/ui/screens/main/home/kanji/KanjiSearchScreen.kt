package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.KanjiStorage
import fr.mrantoine.franji.ui.components.navigation.BottomBar
import fr.mrantoine.franji.ui.components.navigation.HomeTopBar
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun KanjiSearchScreen(
    modifier: Modifier,
    search_text: String,
    onClick: (String) -> Unit
) {
    var kanjiList by remember { mutableStateOf<List<Kanji>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(search_text) {
        loading = true
        delay(500)

        kanjiList = if (search_text.isNotEmpty()) {
            withContext(kotlinx.coroutines.Dispatchers.IO) {
                KanjiStorage.search(search_text)
            }
        } else {
            emptyList()
        }

        loading = false
    }

    Box(
        modifier = modifier
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
            search_text.isNotEmpty() -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(kanjiList, key = { it.id }) { kanji ->

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(Dimens.s)
                                .clickable {
                                    onClick(kanji.id)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = kanji.kanji,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.SemiBold
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
                    Text(text = "Rechercher un Kanji")
                }
            }
        }

    }
}