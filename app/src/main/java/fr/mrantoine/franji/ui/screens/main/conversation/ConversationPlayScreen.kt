package fr.mrantoine.franji.ui.screens.main.conversation

import android.graphics.BitmapFactory
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.ui.components.navigation.TopBar
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Conversation
import fr.mrantoine.franji.storage.ConversationElement
import fr.mrantoine.franji.storage.ConversationStorage
import fr.mrantoine.franji.storage.TtsStorage
import fr.mrantoine.franji.ui.components.ChoiceButton
import fr.mrantoine.franji.ui.components.DisplayTextBox
import fr.mrantoine.franji.ui.components.FlowerDivider
import fr.mrantoine.franji.ui.components.InfoBox
import fr.mrantoine.franji.ui.components.SpeechBubble
import fr.mrantoine.franji.ui.components.ThemeColor

@Composable
fun ConversationPlayingScreen(
    navController: NavController,
    convId: String
) {
    var waiting by remember { mutableStateOf(false) }

    val context = LocalContext.current
    fun loadAsset(path: String) = context.assets.open(path).use {
        BitmapFactory.decodeStream(it).asImageBitmap()
    }

    var conversation by remember { mutableStateOf(Conversation()) }
    var clickedChoices by remember { mutableStateOf<Set<ConversationElement>>(emptySet()) }
    var gameIndex by remember { mutableStateOf<Int?>(null) }




    var questions by remember { mutableStateOf<List<String>>(emptyList()) }
    var answers by remember { mutableStateOf<List<String>>(emptyList()) }

    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    var selection by remember { mutableStateOf<Map<String, ConversationElement>>(emptyMap()) }
    var choices by remember { mutableStateOf< Map<String, List<ConversationElement>>>(emptyMap()) }
    var key by remember { mutableStateOf("") }



    fun game() {

        fun extractBetweenBraces(text: String): String =
            text.substringAfter("{", "").substringBefore("}")


        fun replaceInBraces(text: String, replacement: String): String =
            Regex("\\{[^}]*\\}").replace(text) { "{$replacement}" }

        clickedChoices = emptySet()
        question = questions[gameIndex!!]


        key = extractBetweenBraces(answers[gameIndex!!])


        answer = replaceInBraces(answers[gameIndex!!], selection[key]?.jp ?: "")
        TtsStorage.speak(
            text = question
        )
    }

    fun selected() {
        TtsStorage.speak(
            answer.replace(Regex("[{}]"), ""))
        waiting = true
    }


    fun replacePlaceholders(
        text: String,
        values: Map<String, ConversationElement>
    ): String {
        var result = text

        values.forEach { (key, value) ->
            result = result.replace("{$key}", "{${value.fr}}")
        }

        return result
    }






    LaunchedEffect(Unit) {
        conversation = ConversationStorage.getConversations(convId) ?: Conversation()

        val parts = conversation.parts




        questions = conversation.phases.map { it -> it.random()[0] }
        answers = conversation.phases.map { it -> it.random()[1] }
        selection = conversation.parts
            .mapNotNull { part ->
                part.elements.randomOrNull()?.let { element -> part.name to element }
            }
            .toMap()


            conversation.parts.map { it.elements.randomOrNull() }

        choices = conversation.parts
            .mapNotNull { part ->
                val chosen = selection[part.name] ?: return@mapNotNull null
                val sameOther = part.elements.filter { it != chosen }.randomOrNull()
                val fromOtherPart = conversation.parts
                    .filter { it !== part && it.elements.size >= 2 }
                    .randomOrNull()
                    ?.elements
                    ?.shuffled()
                    ?.take(2)
                    .orEmpty()

                part.name to (listOfNotNull(chosen, sameOther) + fromOtherPart).shuffled()
            }
            .toMap()

        gameIndex = 0
        game()
    }

    Scaffold(
        containerColor = ThemeColor.Cream,
        topBar = {
            val backDispatcher =
                LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = if(gameIndex == null) "" else conversation.title,
                showBack = true,
                showMenu = false,
                onBackClick = { backDispatcher?.onBackPressed() },
                navController = navController
            )
        }
    ) { innerPadding ->

        if(gameIndex != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Image(
                    bitmap = loadAsset("conversations/bottom.png"),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clipToBounds()
                    ) {
                        Image(
                            bitmap = loadAsset(conversation.backgroundImagePath),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )

                        Image(
                            bitmap = loadAsset(conversation.characterImagePath),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .offset(x = 20.dp, y = 10.dp)
                                .height(250.dp)
                        )

                        Box(
                            modifier = Modifier.padding(12.dp).align(Alignment.CenterEnd)
                                .fillMaxWidth(0.7f).offset(y = -45.dp)
                        ) {
                            SpeechBubble() {
                                Text(
                                    text = question,
                                    color = ThemeColor.Brown,
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 17.sp * 1.4f
                                )
                            }
                        }


                        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                            Image(
                                bitmap = loadAsset(conversation.delimiterImagePath),
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (waiting) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    DisplayTextBox(
                                        text = answer,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(12.dp)) {

                        FlowerDivider()
                        Spacer(modifier = Modifier.height(12.dp))


                        if(waiting) {
                            ChoiceButton(
                                text = "Suivant",
                                onClick = {
                                    gameIndex = (gameIndex!! + 1) % questions.size
                                    if (gameIndex == 0) {
                                        navController.navigate(Screen.ConversationList.route)
                                    }
                                    else {
                                        waiting = false
                                        game()
                                    }
                                }
                            )
                        }
                        else {

                            val choicesForKey = choices[key]
                            if(choicesForKey != null) {
                                choicesForKey.forEach { choice ->
                                    ChoiceButton(
                                        text = choice.jp,
                                        disabled = choice in clickedChoices,
                                        onClick = {
                                            if(choice.jp == selection[key]?.jp) {
                                                selected()
                                            } else {
                                                clickedChoices = clickedChoices + choice
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            } else {
                                ChoiceButton(
                                    text = answers[gameIndex!!],
                                    onClick = {
                                        selected()
                                    }
                                )
                            }



                        }
                        Spacer(modifier = Modifier.weight(1f))
                        InfoBox(text = replacePlaceholders(conversation.objective, selection))
                    }
                }
            }
        }

    }
}