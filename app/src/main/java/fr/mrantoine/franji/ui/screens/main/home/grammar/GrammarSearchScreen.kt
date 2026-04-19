package fr.mrantoine.franji.ui.screens.main.home.grammar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun GrammarSearchScreen(
    modifier: Modifier,
    search_text: String,
    onClick: (String) -> Unit
) {
    var grammarList by remember { mutableStateOf<List<Grammar>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }

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
                LazyColumn {

                    grammarList.forEach { grammar ->
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onClick(grammar.id)
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