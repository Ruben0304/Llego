package com.llego.multiplatform.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.llego.multiplatform.data.model.Product
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
    Section(
        title = "Podrías necesitar",
        onSeeMoreClick = onSeeMoreClick,
        modifier = modifier
    ) {
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