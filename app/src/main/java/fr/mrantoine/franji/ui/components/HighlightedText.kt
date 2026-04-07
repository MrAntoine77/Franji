package fr.mrantoine.franji.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun HighlightedText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    highlightColor: Color = MaterialTheme.colorScheme.primary,
    fontSize: androidx.compose.ui.unit.TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal
) {
    val annotatedString = buildAnnotatedString {
        var inBraces = false
        text.forEach { char ->
            when (char) {
                '{' -> inBraces = true
                '}' -> inBraces = false
                else -> {
                    if (inBraces) {
                        withStyle(style = SpanStyle(color = highlightColor)) {
                            append(char)
                        }
                    } else {
                        withStyle(style = SpanStyle(color = color)) {
                            append(char)
                        }
                    }
                }
            }
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
    )
}