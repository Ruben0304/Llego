package com.llego.multiplatform.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.AsyncImagePainter
import com.llego.multiplatform.ui.components.atoms.CounterControls
import com.llego.multiplatform.ui.theme.shapes.CurvedBottomShape


@Composable
fun ProductCard(
    imageUrl: String,
    name: String,
    shop: String,
    weight: String,
    price: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp),
        shape = CurvedBottomShape(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Parte superior (contenido del producto)
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier.size(84.dp),
                    contentScale = ContentScale.Fit
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        is AsyncImagePainter.State.Error -> {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Error loading image",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Reservar siempre el espacio para 2 líneas de título
                Box(
                    modifier = Modifier
                        .height(37.dp) // Altura fija para 2 líneas
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp,
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            )
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "($shop)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 12.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = weight,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onTertiary,
                        lineHeight = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Controles de contador - padding proporcional
            CounterControls(
                count = count,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                modifier = Modifier.padding(bottom = 8.dp)
                    .height(36.dp)
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

