package com.llego.multiplatform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Coffee

import androidx.compose.material.icons.filled.Icecream
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.llego.multiplatform.ui.components.atoms.CartButton
import com.llego.multiplatform.ui.components.atoms.SearchBar
import com.llego.multiplatform.ui.components.background.CurvedBackground
import com.llego.multiplatform.ui.components.sections.ProductsSection
import com.llego.multiplatform.ui.components.molecules.SemicircularSlider
import com.llego.multiplatform.ui.components.molecules.CategoryData
import com.llego.multiplatform.ui.theme.LlegoTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

data class Product(
    val id: Int,
    val name: String,
    val shop: String,
    val weight: String,
    val price: String,
    val imageUrl: String
)

@Composable
@Preview
fun App() {
    LlegoTheme {
        BoxWithConstraints {

            val screenWidth = maxWidth
            val screenHeight = maxHeight
            
            val cardWidth = screenWidth * 0.30f
            val cardHeight = screenHeight * 0.28f
            val buttonHeight = screenHeight * 0.063f
            
            val categories = remember {
                listOf(
                    CategoryData(Icons.Default.LocalPizza, "Italiana"),
                    CategoryData(Icons.Default.Restaurant, "Carnes"),
                    CategoryData(Icons.Default.Grass, "Vegetales"),
                    CategoryData(Icons.Default.LocalDining, "Gourmet"),
                    CategoryData(Icons.Default.Cake, "Postres"),
                    CategoryData(Icons.Default.Coffee, "Bebidas"),
                    CategoryData(Icons.Default.Icecream, "Helados")
                )
            }
            
            val products = remember {
                listOf(
                    Product(
                        1,
                        "Beetroot",
                        "Local Shop",
                        "500 gm.",
                        "17.29$",
                        "https://images.unsplash.com/photo-1570197788417-0e82375c9371?w=300"
                    ),
                    Product(
                        2,
                        "Italian Avocado",
                        "Fresh Market",
                        "450 gm.",
                        "14.29$",
                        "https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=300"
                    ),
                    Product(
                        3,
                        "Red Tomatoes",
                        "Garden Fresh",
                        "1 kg.",
                        "8.50$",
                        "https://images.unsplash.com/photo-1546470427-e26264be0b0d?w=300"
                    ),
                    Product(
                        4,
                        "Green Broccoli",
                        "Organic Store",
                        "750 gm.",
                        "12.99$",
                        "https://images.unsplash.com/photo-1459411621453-7b03977f4bfc?w=300"
                    ),
                    Product(
                        5,
                        "Yellow Bell Pepper",
                        "Local Shop",
                        "300 gm.",
                        "6.75$",
                        "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?w=300"
                    ),
                    Product(
                        6,
                        "Fresh Carrots",
                        "Farm Direct",
                        "1 kg.",
                        "5.99$",
                        "https://images.unsplash.com/photo-1445282768818-728615cc910a?w=300"
                    ),
                    Product(
                        7,
                        "Purple Cabbage",
                        "Organic Store",
                        "800 gm.",
                        "9.25$",
                        "https://images.unsplash.com/photo-1594282486552-05b4d80fbb9f?w=300"
                    ),
                    Product(
                        8,
                        "Sweet Corn",
                        "Garden Fresh",
                        "4 pieces",
                        "7.50$",
                        "https://images.unsplash.com/photo-1551754655-cd27e38d2076?w=300"
                    ),
                    Product(
                        9,
                        "Red Onions",
                        "Local Shop",
                        "1 kg.",
                        "4.99$",
                        "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=300"
                    ),
                    Product(
                        10,
                        "Fresh Spinach",
                        "Organic Store",
                        "250 gm.",
                        "3.75$",
                        "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=300"
                    ),
                    Product(
                        11,
                        "Green Cucumber",
                        "Fresh Market",
                        "500 gm.",
                        "4.25$",
                        "https://images.unsplash.com/photo-1449300079323-02e209d9d3a6?w=300"
                    ),
                    Product(
                        12,
                        "Orange Pumpkin",
                        "Farm Direct",
                        "2 kg.",
                        "11.99$",
                        "https://images.unsplash.com/photo-1570586437263-ab629fccc818?w=300"
                    )
                )
            }
            
            CurvedBackground {
                val productCounts = remember {
                    mutableStateMapOf<Int, Int>().apply {
                        products.forEach { product -> this[product.id] = 0 }
                    }
                }
                Column(modifier = Modifier.padding(top=30.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .weight(1f)
                                .height(buttonHeight)
                        )
                        CartButton(
                            modifier = Modifier.size(buttonHeight)
                        )
                    }

                    Spacer(modifier = Modifier.height(110.dp))
                    
                    SemicircularSlider(
                        categories = categories,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp),
//                        horizontalArrangement = Arrangement.SpaceEvenly,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text("Small", style = MaterialTheme.typography.bodySmall)
//                            Loader(size = LoaderSize.Small)
//                        }
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text("Medium", style = MaterialTheme.typography.bodySmall)
//                            Loader(size = LoaderSize.Medium)
//                        }
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text("Large", style = MaterialTheme.typography.bodySmall)
//                            Loader(size = LoaderSize.Large)
//                        }
//                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ProductsSection(
                        products = products,
                        productCounts = productCounts,
                        onIncrement = { productId ->
                            productCounts[productId] = (productCounts[productId] ?: 0) + 1
                        },
                        onDecrement = { productId ->
                            val currentCount = productCounts[productId] ?: 0
                            if (currentCount > 0) {
                                productCounts[productId] = currentCount - 1
                            }
                        },
                        cardWidth = cardWidth,
                        cardHeight = cardHeight,
                        onSeeMoreClick = { 
                            // Handle see more click
                        }
                    )
                }
            }
        }
    }
}