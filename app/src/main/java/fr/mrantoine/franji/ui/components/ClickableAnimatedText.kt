package fr.mrantoine.franji.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ClickableAnimatedText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    clickedColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    lineHeight: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textAlign: TextAlign = TextAlign.Start,
    gradientDuration: Int = 1800,
    onClick: () -> Unit = {},
) {
    var isClicked by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Animation de couleur
    val animatedColor by animateColorAsState(
        targetValue = if (isClicked) clickedColor else color,
        animationSpec = tween(
            durationMillis = if (isClicked) 0 else gradientDuration,
            easing = LinearOutSlowInEasing
        ),
        label = "textColorAnimation"
    )

    HighlightedText(
        text = text,
        modifier = modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            {
                isClicked = true
                onClick()
                scope.launch {
                    delay(150)
                    isClicked = false
                }
            },
        color = animatedColor,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        textAlign = textAlign
    )
}