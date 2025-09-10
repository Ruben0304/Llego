package com.llego.multiplatform.ui.components.organisms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.llego.multiplatform.ui.components.atoms.CategoryItem
import kotlin.math.*

data class CategoryData(
    val icon: ImageVector,
    val text: String
)

private fun calculateSemicircleY(scrollOffset: Float, maxOffset: Float): Float {
    // Normalizar el offset de -1 a 1
    val normalizedOffset = (scrollOffset / maxOffset).coerceIn(-1f, 1f)

    // Crear semicírculo clásico usando la ecuación del círculo: y = sqrt(r² - x²)
    // Radio más pronunciado similar a curved background
    val radius = 180f
    val x = normalizedOffset * radius
    val absX = abs(x)

    return if (absX <= radius) {
        // Semicírculo hacia abajo - mantener la curva pronunciada original
        sqrt(radius * radius - x * x) - radius
    } else {
        // Fuera del semicírculo, continuar la curva suavemente
        -radius
    }
}

@Composable
fun SemicircularSlider(
    categories: List<CategoryData>,
    modifier: Modifier = Modifier,
    itemSize: Int = 70,
    curveStart: Float = 0.25f,
    curveEnd: Float = 0.25f,
    curveInclination: Float = 0.08f,
    parentWidth: Float = 0f,
    parentHeight: Float = 0f
) {
    // Crear lista infinita repitiendo las categorías
    val repeatedCategories = remember(categories) {
        if (categories.isEmpty()) emptyList()
        else {
            // Repetir la lista múltiples veces para scroll infinito
            val repetitions = 1000
            buildList {
                repeat(repetitions) {
                    addAll(categories)
                }
            }
        }
    }

    // Calcular índice inicial (centro de la lista repetida)
    val initialIndex = remember(categories) {
        if (categories.isEmpty()) 0
        else (repeatedCategories.size / 2) - (categories.size / 2)
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val density = LocalDensity.current

    // Efecto para mantener el scroll en el centro (infinite scroll)
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.collect { firstVisibleIndex ->
            val totalItems = repeatedCategories.size
            val originalSize = categories.size

            if (originalSize > 0) {
                // Si estamos muy cerca del principio, saltar al centro-final
                if (firstVisibleIndex < originalSize * 2) {
                    val newIndex = firstVisibleIndex + (originalSize * 300)
                    listState.scrollToItem(newIndex)
                }
                // Si estamos muy cerca del final, saltar al centro-principio
                else if (firstVisibleIndex > totalItems - (originalSize * 2)) {
                    val newIndex = firstVisibleIndex - (originalSize * 300)
                    listState.scrollToItem(newIndex)
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp) // Aumentado para dar espacio al semicírculo
            .graphicsLayer {
                clip = false
            },
        contentAlignment = Alignment.Center
    ) {
        val screenWidthDp = maxWidth
        val itemWidthDp = (itemSize + 20).dp // itemSize + spacing

        // Calcular padding para mostrar solo 4 elementos
        val visibleItems = 4
        val totalWidthForItems = itemWidthDp * visibleItems
        val sidePadding = (screenWidthDp - totalWidthForItems) / 2f + itemWidthDp

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(
                horizontal = sidePadding,
                vertical = 40.dp // Padding aumentado para el semicírculo
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .graphicsLayer {
                    clip = false
                }
        ) {
            itemsIndexed(repeatedCategories) { index, category ->
                val scrollOffset by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.width / 2f

                        // Buscar el item actual o calcular su posición estimada
                        val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

                        if (itemInfo != null) {
                            // El item está visible, usar su posición real
                            val itemCenter = itemInfo.offset + itemInfo.size / 2f
                            (itemCenter - viewportCenter) / density.density
                        } else {
                            // El item no está visible, estimar su posición para transición suave
                            val visibleItems = layoutInfo.visibleItemsInfo
                            if (visibleItems.isNotEmpty()) {
                                val firstVisible = visibleItems.first()
                                val lastVisible = visibleItems.last()
                                val avgItemSize = (itemSize + 20) * density.density

                                when {
                                    index < firstVisible.index -> {
                                        // Item está antes del viewport
                                        val distance = (firstVisible.index - index) * avgItemSize
                                        val estimatedCenter = firstVisible.offset - distance + avgItemSize / 2f
                                        (estimatedCenter - viewportCenter) / density.density
                                    }
                                    index > lastVisible.index -> {
                                        // Item está después del viewport
                                        val distance = (index - lastVisible.index) * avgItemSize
                                        val estimatedCenter = lastVisible.offset + lastVisible.size + distance - avgItemSize / 2f
                                        (estimatedCenter - viewportCenter) / density.density
                                    }
                                    else -> 0f
                                }
                            } else {
                                0f
                            }
                        }
                    }
                }

                // Limitar el offset para evitar valores extremos
                val clampedOffset = scrollOffset.coerceIn(-200f, 200f)

                // Calcular posición Y semicircular pronunciada
                val yOffset = calculateSemicircleY(clampedOffset, 200f)

                // Efectos visuales suaves
                val normalizedOffset = (clampedOffset / 250f).coerceIn(-1f, 1f)
                val rotation = -normalizedOffset * 8f
                val scale = 1f - (abs(normalizedOffset) * 0.15f).coerceAtMost(0.3f)
                val alpha = (1f - abs(normalizedOffset) * 0.4f).coerceAtLeast(0.3f)

                CategoryItem(
                    text = category.text,
                    circleSize = itemSize,
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = yOffset
                            rotationZ = rotation
                            scaleX = scale
                            scaleY = scale
                            clip = false
                        }
                )
            }
        }
    }
}