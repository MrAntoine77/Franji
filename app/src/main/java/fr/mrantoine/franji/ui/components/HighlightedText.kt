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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun HighlightedText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    highlightColor: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = 16.sp,
    lineHeight: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
    textAlign: TextAlign = TextAlign.Start,
    highlightBold: Boolean = false
) {

    val annotatedString = buildAnnotatedString {
        var inBraces = false
        val buffer = StringBuilder()

        fun flush(normal: Boolean) {
            if (buffer.isNotEmpty()) {
                val style = if (normal) {
                    SpanStyle(
                        color = color,
                        fontWeight = fontWeight
                    )
                } else {
                    SpanStyle(
                        color = highlightColor,
                        fontWeight = if (highlightBold) FontWeight.Bold else fontWeight
                    )
                }

                withStyle(style) {
                    append(buffer.toString())
                }
                buffer.clear()
            }
        }

        text.forEach { char ->
            when (char) {
                '{' -> {
                    flush(true)
                    inBraces = true
                }

                '}' -> {
                    flush(false)
                    inBraces = false
                }

                else -> buffer.append(char)
            }
        }

        flush(!inBraces)
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        textAlign = textAlign,
        lineHeight = lineHeight
    )
}