package fr.mrantoine.franji.ui.screens.main.quizz.grammar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.storage.Grammar
import fr.mrantoine.franji.storage.GrammarItem
import fr.mrantoine.franji.storage.GrammarStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.ClickableAnimatedText
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.theme.Dimens
import java.util.UUID

data class SelectableItem(
    val id: String? = null,
    val label: String = ""
)


@Composable
fun selectableCardFlow(
    items: List<String>,
    maxItems: Int,
    target: List<String>,
    passed: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onReveal: (Boolean) -> Unit = {}
) {

    val mappedItems = remember(items) {
        items.map {
            SelectableItem(
                id = UUID.randomUUID().toString(),
                label = it
            )
        }
    }

    val selected = remember {
        mutableStateListOf<SelectableItem>().apply {
            repeat(maxItems) { add(SelectableItem()) }
        }
    }

    fun toggle(item: SelectableItem) {
        val index = selected.indexOfFirst { it.id == item.id }

        if (index != -1) {
            selected[index] = SelectableItem()
        } else {
            val emptyIndex = selected.indexOfFirst { it.id == null }
            if (emptyIndex != -1) {
                selected[emptyIndex] = item
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            selected
                .forEach { item ->
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable {
                            toggle(item)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.label,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            mappedItems.forEach { item ->
                val isSelected = (selected.contains(item))

                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable {
                            toggle(item)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.label,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(Dimens.l))
        val enabled = (selected.all { it.id != null })
        ThemedButton(
            text = "Valider",
            onClick = {
                val labels = selected.map { it.label }
                for (i in selected.indices) {
                    if(labels != target) {
                        selected[i] = SelectableItem()
                        passed.value = false
                    } else {
                        onReveal(true)
                    }
                }
            },
            enabled = enabled
        )
    }
}


fun splitAndMask(input: String): List<String> {
    val result = mutableListOf<String>()
    val regex = "\\{[^}]*\\}".toRegex()

    var lastIndex = 0

    regex.findAll(input).forEach { match ->
        val range = match.range

        if (lastIndex < range.first) {
            result.add(input.substring(lastIndex, range.first))
        }

        result.add("{_}")

        lastIndex = range.last + 1
    }

    if (lastIndex < input.length) {
        result.add(input.substring(lastIndex))
    }

    return result
}


@Composable
fun QuizzGrammarFr3Jp(
    grammarId: String,
    onNext: () -> Unit = {},
    passed: MutableState<Boolean>
) {
    var grammar by remember { mutableStateOf(Grammar()) }
    var grammarItem by remember { mutableStateOf(GrammarItem()) }
    var elementsList by remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current

    var isRevealed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        grammar = GrammarStorage.getGrammarById(context, grammarId)
        grammarItem = grammar.examples.random()

        elementsList = (
                GrammarStorage.get3RandomGrammarElement(grammarId)
                    .flatMap { it.examples.random().elements.jp } +
                        grammarItem.elements.jp
                ).shuffled()
    }
    key(grammarItem) {
        if (grammarItem != GrammarItem()) {
            Column() {
                Text(
                    text = grammarItem.fr,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.width(Dimens.s))
                if(!isRevealed) {
                    HighlightedText(
                        text = splitAndMask(grammarItem.jp).joinToString(" "),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        lineHeight = 36.sp
                        )
                    Spacer(modifier = Modifier.width(Dimens.s))
                    selectableCardFlow(
                        items = elementsList,
                        target = grammarItem.elements.jp,
                        maxItems = grammarItem.elements.jp.size,
                        passed = passed
                    ) { reveal ->
                        TtsStorage.speak(grammarItem.lecture.kana)
                        isRevealed = reveal
                    }
                }
                else {
                    ClickableAnimatedText(
                        text = grammarItem.jp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                        onClick = { TtsStorage.speak(grammarItem.lecture.kana) }
                    )
                    ClickableAnimatedText(
                        text = grammarItem.lecture.kana,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        onClick = { TtsStorage.speak(grammarItem.lecture.kana) }
                    )
                    Spacer(modifier = Modifier.height(Dimens.xl))
                    Text(
                        text = grammar.title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 28.sp
                    )
                    Text(
                        text = grammar.subtitle,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(Dimens.m))
                    Text(
                        text = grammar.desc,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ThemedButton(
                        text = "Suivant",
                        onClick = {
                            onNext()
                        }
                    )
                }
            }
        }
    }
}