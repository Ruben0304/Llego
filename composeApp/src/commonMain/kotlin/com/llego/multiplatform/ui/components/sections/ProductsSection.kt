package com.llego.multiplatform.ui.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.llego.multiplatform.Product
import com.llego.multiplatform.ui.components.molecules.ProductCard

@Composable
fun ProductsSection(
    products: List<Product>,
    productCounts: Map<Int, Int>,
    onIncrement: (Int) -> Unit,
    onDecrement: (Int) -> Unit,
    cardWidth: Dp,
    cardHeight: Dp,
    onSeeMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Podrías necesitar",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            TextButton(
                onClick = onSeeMoreClick
            ) {
                Text(
                    text = "Ver más",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    imageUrl = product.imageUrl,
                    name = product.name,
                    shop = product.shop,
                    weight = product.weight,
                    price = product.price,
                    count = productCounts[product.id] ?: 0,
                    onIncrement = { onIncrement(product.id) },
                    onDecrement = { onDecrement(product.id) },
                    modifier = Modifier.size(width = cardWidth, height = cardHeight)
                )
            }
        }
    }
}