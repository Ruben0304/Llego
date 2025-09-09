package com.llego.multiplatform.ui.theme.shapes

import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// Constantes unificadas para curvatura consistente
object CurveConstants {
    val CORNER_RADIUS = 12.dp
    val BOTTOM_CORNER_RADIUS = 6.dp // Menos redondeado que arriba
    val BOTTOM_CURVE_HEIGHT = 8.dp
    val BOTTOM_DIP = 20.dp // Curvatura balanceada para buen centrado
}

class CurvedBottomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val cornerRadius = with(density) { CurveConstants.CORNER_RADIUS.toPx() }
                val bottomCornerRadius = with(density) { CurveConstants.BOTTOM_CORNER_RADIUS.toPx() }
                val bottomDip = with(density) { CurveConstants.BOTTOM_DIP.toPx() }

                val w = size.width
                val h = size.height

                // Comenzar en esquina superior izquierda redondeada
                moveTo(0f, cornerRadius)
                quadraticTo(0f, 0f, cornerRadius, 0f)
                
                // Línea superior recta
                lineTo(w - cornerRadius, 0f)
                
                // Esquina superior derecha redondeada
                quadraticTo(w, 0f, w, cornerRadius)

                // Lado derecho recto hasta comenzar curva inferior
                lineTo(w, h - bottomDip * 0.5f)
                
                // Curva inferior pronunciada
                cubicTo(
                    // Control point 1: transición suave desde lado derecho con esquina integrada
                    w - bottomCornerRadius * 0.5f, h + bottomDip * 0.4f,
                    // Control point 2: transición suave hacia lado izquierdo con esquina integrada
                    bottomCornerRadius * 0.5f, h + bottomDip * 0.4f,
                    // End point: lado izquierdo
                    0f, h - bottomDip * 0.5f
                )

                // Lado izquierdo hasta esquina superior
                lineTo(0f, cornerRadius)
                
                close()
            }
        )
    }
}

// Forma para CounterControls que coincide con ProductCard en la parte inferior
class CounterControlsShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            Path().apply {
                val cornerRadius = with(density) { CurveConstants.CORNER_RADIUS.toPx() }
                val bottomCornerRadius = with(density) { CurveConstants.BOTTOM_CORNER_RADIUS.toPx() }
                val topCurveDepth = with(density) { 6.dp.toPx() }
                val bottomDip = with(density) { CurveConstants.BOTTOM_DIP.toPx() }

                val w = size.width
                val h = size.height

                // Comenzar en esquina superior izquierda redondeada
                moveTo(0f, cornerRadius)
                quadraticTo(0f, 0f, cornerRadius, 0f)
                
                // Curva superior más pronunciada
                quadraticTo(
                    w / 2f, topCurveDepth * 1.5f,
                    w - cornerRadius, 0f
                )
                
                // Esquina superior derecha redondeada
                quadraticTo(w, 0f, w, cornerRadius)
                
                // Lado derecho recto hasta comenzar curva inferior
                lineTo(w, h - bottomDip * 0.4f)
                
                // Curva inferior más pronunciada que ProductCard
                cubicTo(
                    // Control point 1: transición suave desde lado derecho con esquina integrada
                    w - bottomCornerRadius * 0.3f, h + bottomDip * 0.6f,
                    // Control point 2: transición suave hacia lado izquierdo con esquina integrada
                    bottomCornerRadius * 0.3f, h + bottomDip * 0.6f,
                    // End point: lado izquierdo
                    0f, h - bottomDip * 0.4f
                )

                // Lado izquierdo hasta esquina superior
                lineTo(0f, cornerRadius)
                
                close()
            }
        )
    }
}