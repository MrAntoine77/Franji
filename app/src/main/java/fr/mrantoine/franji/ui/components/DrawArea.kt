package fr.mrantoine.franji.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DrawArea(angles: List<Float>, isRevealed: MutableState<Boolean>) {
    var paths by remember { mutableStateOf(listOf<List<Offset>>()) }
    var currentPath by remember { mutableStateOf(listOf<Offset>()) }

    var drawIndex by remember { mutableStateOf(0) }

    fun checkIndex(path: List<Offset>) {
        if (path.size >= 2) {
            val start = path.first()
            val end = path.last()

            val deltaX = end.x - start.x
            val deltaY = end.y - start.y

            val angleRad = atan2(deltaY, deltaX)
            var angleDeg = (angleRad * 180 / PI + 360) % 360

            // Convertir CCW (atan2) en CW
            angleDeg = (360 - angleDeg) % 360

            val targetAngle = angles[drawIndex]

            val diff = min(
                abs(angleDeg - targetAngle),
                360 - abs(angleDeg - targetAngle)
            )

            if (diff <= 22.5) {
                drawIndex+=1
                if(drawIndex == angles.size) {
                    isRevealed.value = true
                }
            } else {
                paths = emptyList()
                currentPath = emptyList()
                drawIndex = 0
            }
        }


    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                val canvasSize = size
                detectDragGestures(
                    onDragStart = { offset ->
                        if (isInsideCanvas(offset, canvasSize)) {
                            currentPath = listOf(offset)
                        }
                    },
                    onDrag = { change, _ ->
                        val pos = change.position
                        if (isInsideCanvas(pos, canvasSize)) {
                            currentPath = currentPath + pos
                        } else if (currentPath.isNotEmpty()) {
                            paths = paths + listOf(currentPath)
                            checkIndex(currentPath)
                            currentPath = emptyList()
                        }
                    },
                    onDragEnd = {
                        if (currentPath.isNotEmpty()) {
                            paths = paths + listOf(currentPath)
                            checkIndex(currentPath)
                            currentPath = emptyList()

                        }
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            paths.forEach { path ->
                for (i in 0 until path.size - 1) {
                    drawLine(
                        color = Color.Black,
                        start = path[i],
                        end = path[i + 1],
                        strokeWidth = 8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            for (i in 0 until currentPath.size - 1) {
                drawLine(
                    color = Color.Black,
                    start = currentPath[i],
                    end = currentPath[i + 1],
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private fun isInsideCanvas(offset: Offset, size: IntSize): Boolean {
    return offset.x in 0f..size.width.toFloat() && offset.y in 0f..size.height.toFloat()
}