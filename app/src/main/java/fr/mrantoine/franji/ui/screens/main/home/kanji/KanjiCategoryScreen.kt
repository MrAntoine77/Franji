package fr.mrantoine.franji.ui.screens.main.home.kanji

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    modifier: Modifier,
    categoryPath: String,
    onClick: (String) -> Unit,
) {
    val context = LocalContext.current

    var kanjiList by remember { mutableStateOf<List<Kanji>>(emptyList()) }

    LaunchedEffect(categoryPath) {
        val ids = CategoryStorage.getCategoryByPath(context, categoryPath)

        kanjiList = ids.map { id ->
            KanjiStorage.getKanjiById(context, id)
        }
    }

    val title = categoryPath.uppercase().replace("/", " ")

    Box(
        modifier = modifier
            .padding(Dimens.m)
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.fillMaxWidth()
        ) {

            item(span = { GridItemSpan(5) }) {
                Text(
                    text = "- $title -",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.m),
                    textAlign = TextAlign.Center
                )
            }

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
}