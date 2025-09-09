package com.llego.multiplatform.ui.components.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import llego.composeapp.generated.resources.Res
import llego.composeapp.generated.resources.Broccoli
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryItem(
    text: String,
    modifier: Modifier = Modifier,
    circleSize: Int = 60
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(circleSize.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Image(
                painter = painterResource(Res.drawable.Broccoli),
                contentDescription = text,
                modifier = Modifier.size((circleSize * 0.4).dp)
            )
        }
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}