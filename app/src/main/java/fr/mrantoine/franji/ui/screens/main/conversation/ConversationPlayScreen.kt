package fr.mrantoine.franji.ui.screens.main.conversation

import android.graphics.BitmapFactory
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.mrantoine.franji.ui.components.HighlightedText
import fr.mrantoine.franji.ui.components.navigation.TopBar
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import fr.mrantoine.franji.Screen
import fr.mrantoine.franji.storage.Conversation
import fr.mrantoine.franji.storage.ConversationElement
import fr.mrantoine.franji.storage.ConversationStorage
import fr.mrantoine.franji.storage.TtsStorage
import kotlin.math.min

/**
 * Palette centralisée de l'écran de conversation.
 * Modifier une valeur ici la change partout.
 */
object ConvColors {
    // Couleurs de base
    val Brown = Color(0xFF59382C)          // texte + contour sombre
    val BrownDark = Color(0xFF58382B)      // contour externe de la bulle
    val Pink = Color(0xFFFBA9AF)           // accents (lignes, bordures, fleur)
    val PinkSalmon = Color(0xFFF9B2A5)     // anneau intermédiaire de la bulle
    val Cream = Color(0xFFFFF0E9)          // fond des boutons
    val CreamLight = Color(0xFFFFEDE3)     // anneau clair de la bulle
    val CreamLighter = Color(0xFFFFF1E9)   // fond intérieur de la bulle
    val ScreenBackground = Color(0xFFFEE6D7)

    // Boîte d'info semi-transparente
    val InfoBoxBackground = Cream.copy(alpha = 0.70f)
    val InfoBoxBorder = Pink.copy(alpha = 0.75f)


    // États désactivés
    val DisabledBackground = Color(0xFFEADFD9)
    val DisabledBorder = Color(0xFFD6BDB5)
    val DisabledText = Color(0xFFAC9690)

    // Alias sémantiques utilisés par les composants
    val Text = Brown
    val Accent = Pink
    val ButtonBackground = Cream
    val ButtonBorder = Pink
}

/**
 * Dimensions / styles communs.
 */
object ConvStyle {
    val PillShape = RoundedCornerShape(100.dp)
    val DefaultFontSize = 22.sp
    val DefaultPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
}


@Composable
fun InfoBox(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ConvColors.InfoBoxBackground,
    borderColor: Color = ConvColors.InfoBoxBorder,
    textColor: Color = ConvColors.Text,
    fontSize: TextUnit = 16.sp,
    cornerRadius: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(2.dp, borderColor, shape)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        HighlightedText(
            text = text,
            color = textColor,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            lineHeight = fontSize * 1.4f
        )
    }
}



class SpeechBubbleShape(
    private val tailWidth: Dp = 18.dp,
    private val tailHeight: Dp = 26.dp,
    private val cornerRadius: Dp = 40.dp,
    private val tailPosition: Float = 0.58f   // 0 = haut, 1 = bas
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val tw = with(density) { tailWidth.toPx() }
        val th = with(density) { tailHeight.toPx() }
        // Le rayon ne peut pas dépasser la moitié de la hauteur (= forme "pilule")
        val r = min(with(density) { cornerRadius.toPx() }, size.height / 2f)

        val body = Path().apply {
            addRoundRect(
                RoundRect(
                    left = tw,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    cornerRadius = CornerRadius(r)
                )
            )
        }

        val tipY = size.height * tailPosition
        val baseX = tw + r * 0.3f   // un peu à l'intérieur du corps pour que l'union soit propre

        val tail = Path().apply {
            moveTo(baseX, tipY - th / 2f)
            quadraticTo(tw * 0.5f, tipY - th * 0.35f, 0f, tipY)
            quadraticTo(tw * 0.6f, tipY + th * 0.1f, baseX, tipY + th / 2f)
            close()
        }

        return Outline.Generic(Path.combine(PathOperation.Union, body, tail))
    }
}

@Composable
fun SpeechBubble(
    modifier: Modifier = Modifier,
    tailWidth: Dp = 18.dp,
    fillColor: Color = ConvColors.CreamLighter,
    borderColor: Color = ConvColors.Brown,
    borderWidth: Dp = 2.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    content: @Composable () -> Unit
) {
    val shape = SpeechBubbleShape(tailWidth = tailWidth)

    Box(
        modifier = modifier
            .background(fillColor, shape)
            .border(borderWidth, borderColor, shape)
            .padding(
                start = tailWidth + contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateRightPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}







@Composable
fun FlowerDivider(
    modifier: Modifier = Modifier,
    flowerSize: Dp = 16.dp,
    lineThickness: Dp = 2.dp,
    lineColor: Color = ConvColors.Accent,
    spacing: Dp = 8.dp
) {
    val context = LocalContext.current
    val flowerBitmap = remember {
        context.assets.open("conversations/conversation_flower.png").use {
            BitmapFactory.decodeStream(it).asImageBitmap()
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(lineThickness)
                .background(lineColor, CircleShape)
        )

        Image(
            bitmap = flowerBitmap,
            contentDescription = "Flower divider",
            modifier = Modifier.size(flowerSize),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(lineColor)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(lineThickness)
                .background(lineColor, CircleShape)
        )
    }
}

@Composable
fun DisplayTextBox(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = ConvColors.Text,
    fontSize: TextUnit = ConvStyle.DefaultFontSize,
    textAlign: TextAlign = TextAlign.Center,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
) {
    val shape = ConvStyle.PillShape

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ConvColors.BrownDark, shape)
            .padding(4.dp)
            .background(ConvColors.PinkSalmon, shape)
            .padding(4.dp)
            .background(ConvColors.CreamLight, shape)
            .padding(4.dp)
            .background(ConvColors.CreamLighter, shape)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        HighlightedText(
            text = text,
            color = textColor,
            fontSize = fontSize,
            textAlign = textAlign,
            lineHeight = fontSize * 1.4f
        )
    }
}

@Composable
fun ChoiceButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,

    backgroundColor: Color = ConvColors.ButtonBackground,
    borderColor: Color = ConvColors.ButtonBorder,
    textColor: Color = ConvColors.Text,

    disabledBackgroundColor: Color = ConvColors.DisabledBackground,
    disabledBorderColor: Color = ConvColors.DisabledBorder,
    disabledTextColor: Color = ConvColors.DisabledText,

    fontSize: TextUnit = ConvStyle.DefaultFontSize,
    contentPadding: PaddingValues = ConvStyle.DefaultPadding
) {
    val shape = ConvStyle.PillShape

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (disabled) disabledBorderColor else borderColor, shape)
            .padding(3.dp)
            .background(if (disabled) disabledBackgroundColor else backgroundColor, shape)
            .clip(shape)
            .clickable(
                enabled = !disabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        HighlightedText(
            text = text,
            color = if (disabled) disabledTextColor else textColor,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            lineHeight = fontSize * 1.4f
        )
    }
}



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

    var selection by remember { mutableStateOf<List<ConversationElement>>(emptyList()) }
    var objective by remember { mutableStateOf<Map<String, String>>(emptyMap()) }


    var choices by remember { mutableStateOf<List<List<ConversationElement>>>(emptyList()) }
    var clickedChoices by remember { mutableStateOf<Set<ConversationElement>>(emptySet()) }

    var gameIndex by remember { mutableStateOf<Int?>(null) }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }

    fun game() {
        fun replaceInBraces(text: String, replacement: String): String =
            Regex("\\{[^}]*\\}").replace(text) { "{$replacement}" }

        clickedChoices = emptySet()
        question = conversation.phases[gameIndex!!].random()[0]
        answer = replaceInBraces(conversation.phases[gameIndex!!].random()[1], selection[gameIndex!!].jp)
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
        values: Map<String, String>
    ): String {
        var result = text

        values.forEach { (key, value) ->
            result = result.replace("{$key}", "{$value}")
        }

        return result
    }


    LaunchedEffect(Unit) {
        conversation = ConversationStorage.getConversations(convId) ?: Conversation()

        val parts = conversation.parts

        // Un seul tirage aléatoire par section
        val picked = parts.map { it.elements.random() }

        selection = picked

        // Map : nom de la section -> traduction française du choix
        objective = parts.mapIndexed { index, part ->
            part.name to picked[index].fr
        }.toMap()

        // 4 choix par section
        choices = parts.mapIndexed { index, part ->
            val correct = picked[index]

            // 1 autre réponse de la même section (différente de la bonne)
            val sameSection = part.elements
                .filter { it != correct }
                .randomOrNull()

            // 2 réponses issues d'une autre section aléatoire
            val otherParts = parts.filterIndexed { i, p ->
                i != index && p.elements.isNotEmpty()
            }

            val otherSection = otherParts.randomOrNull()?.elements.orEmpty()
            val distractors = otherSection.shuffled().take(2)

            (listOfNotNull(correct, sameSection) + distractors).shuffled()
        }

        gameIndex = 0

        game()
    }


    val bottomImage = remember { loadAsset("conversations/conv1/bottom.png") }
    val backgroundImage = remember { loadAsset("conversations/conv1/background.png") }
    val delimiterImage = remember { loadAsset("conversations/conv1/delimiter.png") }
    val characterImage = remember { loadAsset("conversations/conv1/character.png") }

    Scaffold(
        containerColor = ConvColors.ScreenBackground,
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                bitmap = bottomImage,
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
                        bitmap = backgroundImage,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    Image(
                        bitmap = characterImage,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .offset(x = 20.dp, y = 10.dp)
                            .height(250.dp)
                    )

                    if(gameIndex != null) {
                        Box(
                            modifier = Modifier.padding(12.dp).align(Alignment.CenterEnd)
                                .fillMaxWidth(0.7f).offset(y = -45.dp)
                        ) {
                            SpeechBubble() {
                                Text(
                                    text = question,
                                    color = ConvColors.Text,
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 17.sp * 1.4f
                                )
                            }
                        }
                    }

                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        Image(
                            bitmap = delimiterImage,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (gameIndex != null && waiting) {
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

                    if(gameIndex != null)
                    {
                        if(waiting) {
                            ChoiceButton(
                                text = "Suivant",
                                onClick = {
                                    gameIndex = (gameIndex!! + 1) % selection.size
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
                            choices[gameIndex!!].forEach { choice ->
                                ChoiceButton(
                                    text = choice.jp,
                                    disabled = choice in clickedChoices,
                                    onClick = {
                                        if(choice.jp == selection[gameIndex!!].jp) {
                                            selected()
                                        } else {
                                            clickedChoices = clickedChoices + choice
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                    }


                    Spacer(modifier = Modifier.weight(1f))
                    InfoBox(text = replacePlaceholders(conversation.objective, objective))
                }
            }
        }
    }
}