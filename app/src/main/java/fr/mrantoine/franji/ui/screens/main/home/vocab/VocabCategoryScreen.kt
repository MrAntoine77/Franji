package fr.mrantoine.franji.ui.screens.main.home.vocab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.CategoryStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.storage.VocabStorage
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun VocabCategoryScreen(
    modifier: Modifier,
    categoryPath: String,
    onClick: (String) -> Unit,
) {

    var vocabListId by remember { mutableStateOf(emptyArray<String>()) }
    val vocabList = remember { mutableStateListOf<Vocab>() }
    val context = LocalContext.current

    LaunchedEffect(categoryPath) {
        vocabListId = CategoryStorage.getCategoryByPath(context, categoryPath)
        vocabList.clear()
        vocabListId.forEach { vocabId ->
            vocabList.add(VocabStorage.getVocabById(context, vocabId))
        }
    }

    val title = categoryPath.uppercase().replace("/", " ")

    LazyColumn(
        modifier = modifier
            .padding(Dimens.m)
    ) {

        item {
            Text(
                text = "- $title -",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.m),
                textAlign = TextAlign.Center
            )
        }

        vocabList.forEach { vocab ->
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClick(vocab.id)
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