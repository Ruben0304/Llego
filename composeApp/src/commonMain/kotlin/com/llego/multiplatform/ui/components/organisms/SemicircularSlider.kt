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
        // Semicírculo hacia abajo
        sqrt(radius * radius - x * x) - radius
    } else {
        -radius // Fuera del semicírculo, mantener en el borde
    }
}

@Composable
fun SemicircularSlider(
    categories: List<CategoryData>,
    modifier: Modifier = Modifier,
    itemSize: Int = 60,
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
            .height(180.dp)
            .graphicsLayer { clip = false },
        contentAlignment = Alignment.Center
    ) {
        val screenWidthDp = maxWidth
        val itemWidthDp = (itemSize + 20).dp // itemSize + spacing
        val itemsVisible = (screenWidthDp / itemWidthDp).toInt()
        
        // Calcular padding para que siempre haya elementos visibles en los lados
        val visibleItemsOnSide = 2 // Cantidad de elementos visibles en cada lado
        val extraPadding = 30.dp // Espacio adicional para elementos transformados
        val sidePadding = if (categories.size > visibleItemsOnSide * 2 + 1) {
            // Padding que permite ver elementos laterales + espacio para transformaciones
            itemWidthDp * visibleItemsOnSide + extraPadding
        } else {
            // Si hay pocos elementos, padding para centrarlos + espacio para transformaciones
            maxOf(16.dp + extraPadding, (screenWidthDp - (categories.size * itemWidthDp)) / 2f + extraPadding)
        }
        
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(
                horizontal = sidePadding,
                vertical = 20.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { clip = false }
        ) {
            itemsIndexed(repeatedCategories) { index, category ->
                val scrollOffset by remember {
                    derivedStateOf {
                        val itemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                        if (itemInfo != null) {
                            val center = listState.layoutInfo.viewportStartOffset + listState.layoutInfo.viewportSize.width / 2f
                            val itemCenter = itemInfo.offset + itemInfo.size / 2f
                            (itemCenter - center) / density.density
                        } else 0f
                    }
                }
                
                // Calcular posición Y semicircular
                val yOffset = calculateSemicircleY(scrollOffset, 200f)
                
                // Efectos visuales suaves
                val normalizedOffset = (scrollOffset / 250f).coerceIn(-1f, 1f)
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