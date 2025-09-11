package com.llego.multiplatform.ui.components.background

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.llego.multiplatform.ui.state.HomeScreenState

@Composable
fun CurvedBackground(
    modifier: Modifier = Modifier,
    homeState: HomeScreenState? = null,
    curveStart: () -> Float = { 0.22f },
    curveEnd: () -> Float = { 0.22f },
    curveInclination: () -> Float = { 0.08f },
    content: @Composable () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.background

    // Calcular valores según el estado
    val actualCurveStart by animateFloatAsState(
        targetValue = when {
            homeState?.isLoading == true -> 1.0f // Sin curva verde en loading
            homeState?.isInSeeMoreMode == true -> 0.245f // Menos espacio verde en ver más (reducción menor)
            else -> curveStart()
        },
        animationSpec = tween(durationMillis = 600)
    )
    
    val actualCurveEnd by animateFloatAsState(
        targetValue = when {
            homeState?.isLoading == true -> 1.0f // Sin curva verde en loading
            homeState?.isInSeeMoreMode == true -> 0.245f // Menos espacio verde en ver más (reducción menor)
            else -> curveEnd()
        },
        animationSpec = tween(durationMillis = 600)
    )
    
    val backgroundOffset by animateFloatAsState(
        targetValue = if (homeState?.isInSeeMoreMode == true) -50f else 0f, // 50dp hacia arriba
        animationSpec = tween(durationMillis = 600)
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = backgroundOffset.dp)
        ) {
            drawCurvedBackground(
                primaryColor = primaryColor,
                surfaceColor = surfaceColor,
                curveStart = actualCurveStart,
                curveEnd = actualCurveEnd,
                curveInclination = curveInclination(),
                showCurve = homeState?.isLoading != true // No mostrar curva en loading
            )
        }
        
        content()
    }
}

private fun DrawScope.drawCurvedBackground(
    primaryColor: androidx.compose.ui.graphics.Color,
    surfaceColor: androidx.compose.ui.graphics.Color,
    curveStart: Float,
    curveEnd: Float,
    curveInclination: Float,
    showCurve: Boolean = true
) {
    val width = size.width
    val height = size.height
    val curveStartY = height * curveStart
    val curveEndY = height * curveEnd

    // Dibujar fondo gris (superficie) completo
    drawRect(
        color = surfaceColor,
        size = size
    )

    // Solo dibujar la curva verde si showCurve es true
    if (showCurve) {
        // Crear el path para la parte verde con curva
        val path = Path().apply {
            // Empezar desde la esquina superior izquierda
            moveTo(0f, 0f)
            // Línea hasta donde empieza la curva
            lineTo(0f, curveStartY)
            
            // Curva con inclinación configurable
            val curveHeight = height * curveInclination
            val controlPointY = curveStartY + curveHeight
            
            // Curva cúbica que simula un semicírculo
            cubicTo(
                width * 0.25f, controlPointY,  // Primer punto de control
                width * 0.75f, controlPointY,  // Segundo punto de control
                width, curveEndY                // Punto final usando curveEnd
            )
            
            // Completar el rectángulo verde
            lineTo(width, 0f)
            close()
        }

        // Dibujar la parte verde
        drawPath(
            path = path,
            color = primaryColor
        )
    }
}