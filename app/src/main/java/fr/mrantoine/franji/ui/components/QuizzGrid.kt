package fr.mrantoine.franji.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.Kanji
import fr.mrantoine.franji.storage.SettingsStorage
import fr.mrantoine.franji.storage.Vocab
import fr.mrantoine.franji.ui.theme.Dimens

@Composable
fun KanjiGrid9Jp(
    items: List<Kanji>,
    onClick: (Kanji) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    kanjiId: String
) {
    val safeItems = (items + List(9 - items.size.coerceAtLeast(0)) { Kanji("") }).take(9)
    val canSelect =  !selectedIds.contains(kanjiId)

    Column {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    val kanji = safeItems[index]
                    val boxColor = if (selectedIds.contains(kanji.id)) MaterialTheme.colorScheme.tertiary else Color.Transparent
                    val textColor = if (selectedIds.contains(kanji.id)) MaterialTheme.colorScheme.secondary else Color.Unspecified

                    Card(
                        modifier = Modifier
                            .size(110.dp)
                            .padding(6.dp)
                            .clickable(enabled = canSelect && !selectedIds.contains(kanji.id)) {
                                if(!selectedIds.contains(kanji.id)) {
                                    onClick(kanji)
                                }
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize().background(boxColor)
                        ) {
                            Text(
                                text = kanji.kanji,
                                fontSize = 32.sp,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KanjiGrid4Kana(
    items: List<Kanji>,
    onClick: (Kanji) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    kanjiId: String
) {
    val safeItems = items.take(4)
    val canSelect = !selectedIds.contains(kanjiId)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        safeItems.forEach { kanji ->

            val isSelected = selectedIds.contains(kanji.id)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            val text = if(SettingsStorage.isRomaji()) kanji.main_lecture.romaji else kanji.main_lecture.kana
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(kanji)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s)
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KanjiGrid4Fr(
    items: List<Kanji>,
    onClick: (Kanji) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    kanjiId: String
) {
    val safeItems = items.take(4)
    val canSelect = !selectedIds.contains(kanjiId)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        safeItems.forEach { kanji ->

            val isSelected = selectedIds.contains(kanji.id)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            val text = kanji.main_lecture.fr
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(kanji)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s)
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VocabGrid4Jp(
    items: List<Vocab>,
    onClick: (Vocab) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    vocabId: String
) {
    val safeItems = items.take(4)
    val canSelect = !selectedIds.contains(vocabId)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        safeItems.forEach { vocab ->

            val isSelected = selectedIds.contains(vocab.id)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            val text = vocab.jp
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(vocab)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s)
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VocabGrid4Fr(
    items: List<Vocab>,
    onClick: (Vocab) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    vocabId: String
) {
    val safeItems = items.take(4)
    val canSelect = !selectedIds.contains(vocabId)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        safeItems.forEach { vocab ->

            val isSelected = selectedIds.contains(vocab.id)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            val text = vocab.fr.replaceFirstChar { it.uppercase() }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(vocab)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s),
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun VocabGrid4Kana(
    items: List<Vocab>,
    onClick: (Vocab) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    vocabId: String
) {
    val safeItems = items.take(4)
    val canSelect = !selectedIds.contains(vocabId)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        safeItems.forEach { vocab ->

            val isSelected = selectedIds.contains(vocab.id)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            val text = if(SettingsStorage.isRomaji()) vocab.lecture.romaji else vocab.lecture.kana
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(vocab)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s)
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GrammarGrid4Fr(
    items: List<Grammar>,
    onClick: (Grammar) -> Unit = {},
    selectedIds: List<String> = emptyList(),
    grammarId: String
) {
    val safeItems = items.take(4)
    val canSelect = !selectedIds.contains(grammarId)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        safeItems.forEach { grammar ->

            val isSelected = selectedIds.contains(grammar.id)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            val text = grammar.subtitle.replaceFirstChar { it.uppercase() }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(grammar)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s),
                    ) {
                        Text(
                            text = text,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ConversationGrd(
    items: List<String>,
    onClick: (String) -> Unit = {},
    selectedIds: List<String> = emptyList()
) {
    val canSelect = true

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { item ->

            val isSelected = selectedIds.contains(item)

            val boxColor =
                if (isSelected) MaterialTheme.colorScheme.tertiary else Color.Transparent

            val textColor =
                if (isSelected) MaterialTheme.colorScheme.secondary else Color.Unspecified

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .clickable(enabled = canSelect && !isSelected) {
                        onClick(item)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(boxColor)
                ){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.s),
                    ) {
                        Text(
                            text = item,
                            fontSize = 20.sp,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}




