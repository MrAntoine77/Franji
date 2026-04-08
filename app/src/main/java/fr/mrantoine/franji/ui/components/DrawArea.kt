package fr.mrantoine.franji.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.mrantoine.franji.R
import fr.mrantoine.franji.storage.KanjiStorage
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min

@Composable
fun DrawArea(angles: List<Float>, isRevealed: MutableState<Boolean>, lottie: String) {
    var paths by remember { mutableStateOf(listOf<List<Offset>>()) }
    var currentPath by remember { mutableStateOf(listOf<Offset>()) }

    var drawIndex by remember { mutableStateOf(0) }
    var fails by remember { mutableStateOf(0) }
    var isLocked by remember { mutableStateOf(false) }
    if (fails > 0) {
        LaunchedEffect(fails) {
            isLocked = true
            kotlinx.coroutines.delay(500)
            isLocked = false
        }
    }

    fun checkIndex(path: List<Offset>) {
        if (path.size >= 2) {
            val start = path.first()
            val end = path.last()

            val deltaX = end.x - start.x
            val deltaY = end.y - start.y

            val angleRad = atan2(deltaY, deltaX)
            var angleDeg = (angleRad * 180 / PI + 360) % 360

            // Convert CCW (atan2) to CW
            angleDeg = (360 - angleDeg) % 360

            val targetAngle = angles[drawIndex]

            val diff = min(
                abs(angleDeg - targetAngle),
                360 - abs(angleDeg - targetAngle)
            )

            if (diff <= 22.5) {
                drawIndex += 1
                if (drawIndex == angles.size) {
                    isRevealed.value = true
                }
            } else {
                fails += 1
                paths = emptyList()
                currentPath = emptyList()
                drawIndex = 0
                isLocked = true

            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(1f)
        ) {
            if (fails >= 1) {
                key(fails) {
                    Lottie(
                        data = lottie,
                        autoPlay = true,
                        color = Color.LightGray,
                        speed = 1.5f,
                        lines = fails)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(48.dp))
                    .background(Color(0x10000000))
                    .align(Alignment.Center)
                    .pointerInput(Unit) {
                        val canvasSize = size
                        detectDragGestures(
                            onDragStart = { offset ->
                                if (!isLocked && isInsideCanvas(offset, canvasSize)) {
                                    currentPath = listOf(offset)
                                }
                            },
                            onDrag = { change, _ ->
                                if (!isLocked) {
                                    val pos = change.position
                                    if (isInsideCanvas(pos, canvasSize)) {
                                        currentPath = currentPath + pos
                                    } else if (currentPath.isNotEmpty()) {
                                        paths = paths + listOf(currentPath)
                                        checkIndex(currentPath)
                                        currentPath = emptyList()
                                    }
                                }
                            },
                            onDragEnd = {
                                if (!isLocked && currentPath.isNotEmpty()) {
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
    }
}
private fun isInsideCanvas(offset: Offset, size: IntSize): Boolean {
    return offset.x in 0f..size.width.toFloat() && offset.y in 0f..size.height.toFloat()
}