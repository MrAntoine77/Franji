package fr.mrantoine.franji.ui.screens.main.conversation

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Conversation
import fr.mrantoine.franji.storage.ConversationStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.ConversationGrd
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.components.navigation.ThemedButton
import fr.mrantoine.franji.ui.components.navigation.TopBar
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch




data class Message(
    val text: String,
    val isSender: Boolean,
    val key: String? = null
)

enum class ConversationPhase {
    SENDER,
    RECEIVER,
    WAITING,
}


fun replacePlaceholders(
    text: String,
    values: Map<String, String>
): String {
    var result = text

    values.forEach { (key, value) ->
        result = result.replace("{$key}", "{$value}")
    }

    return result
}

fun hidePlaceholders(text: String): String {
    val regex = Regex("""\{[^}]*\}""")
    return text.replace(regex, "{...}")
}

@Composable
fun MessageBubble(
    message: Message,
    responseMap: Map<String, String>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isSender)
            Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (message.isSender)
                        MaterialTheme.colorScheme.primary
                    else
                        Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            HighlightedText(
                text = replacePlaceholders(message.text, responseMap),
                color = if (message.isSender) Color.White else Color.Black,
                highlightColor =  if (message.isSender) Color.White else Color.Black,
                highlightBold = true,
            )
        }
    }
}


fun extractFirstBracesContent(input: String): String? {
    val start = input.indexOf('{')
    if (start == -1) return null

    val end = input.indexOf('}', start + 1)
    if (end == -1) return null

    return input.substring(start + 1, end)
}

@Composable
fun ConversationPlayingScreen(
    navController: NavController,
    convId: String
) {
    val chat = remember { mutableStateListOf<Message>() }
    val elementsJp = remember { mutableMapOf<String, String>() }
    val allElementsJp = remember { mutableMapOf<String, List<String>>() }

    val elementsFr = remember { mutableMapOf<String, String>() }
    var index by remember { mutableStateOf(0) }
    var conversation by remember { mutableStateOf(Conversation()) }
    val listState = rememberLazyListState()


    fun next() {
        if(index == chat.size - 1) {
            navController.navigate(Screen.QuizzWorldList.route)
        }
        else {
            index = (index + 1) % chat.size

        }
    }

    LaunchedEffect(Unit) {
        conversation = ConversationStorage.getConversations(convId) ?: Conversation()

        conversation.parts.forEach { (name, elt) ->
            val random = (0 until elt.size).random()
            elementsJp[name] = elt[random].jp
            elementsFr[name] = elt[random].fr

            allElementsJp[name] = elt.map { it.jp }.shuffled()
        }

        conversation.phases.forEach { exchange ->
            exchange.forEachIndexed { index, text ->
                chat.add(Message(
                    text = text,
                    isSender = (index % 2 != 0),
                    key = extractFirstBracesContent(text))
                )
            }
        }
        print(chat)

    }

    LaunchedEffect(index) {
        val text = chat.getOrNull(index)?.text ?: ""
        MainScope().launch {
            TtsStorage.speak(replacePlaceholders(text, elementsJp))
        }
        if (index > 0) {
            listState.animateScrollToItem(index - 1)
        }
    }


    Scaffold(
        topBar = {
            val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = "Conversation Playing",
                showBack = true,
                showMenu = false,
                onBackClick = { backDispatcher?.onBackPressed() },
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            HighlightedText(
                text = replacePlaceholders(conversation.objective, elementsFr),
                textAlign = TextAlign.Justify,
                highlightBold = true
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(chat.take(index + 1)) { message ->
                    MessageBubble(message, elementsJp.toMap())
                }
            }
            val key = chat.getOrNull(index + 1)?.key
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // ← TA TAILLE FIXE
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (key != null) {
                        HighlightedText(
                            text = hidePlaceholders(chat.getOrNull(index + 1)?.text ?: ""),
                            highlightBold = true
                        )

                        ConversationGrd(
                            items = allElementsJp[key] ?: emptyList(),
                            onClick = { next() }
                        )
                    } else {
                        ThemedButton(
                            text = "Suite",
                            onClick = { next() }
                        )
                    }
                }
            }
        }
    }
}