package com.llego.multiplatform.data.repositories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Icecream
import com.llego.multiplatform.ui.components.organisms.CategoryData

class CategoryRepository {
    
    suspend fun getCategories(): List<CategoryData> {
        return listOf(
            CategoryData(Icons.Default.LocalPizza, "Italiana"),
            CategoryData(Icons.Default.Restaurant, "Carnes"),
            CategoryData(Icons.Default.Grass, "Vegetales"),
            CategoryData(Icons.Default.LocalDining, "Gourmet"),
            CategoryData(Icons.Default.Cake, "Postres"),
            CategoryData(Icons.Default.Coffee, "Bebidas"),
            CategoryData(Icons.Default.Icecream, "Helados"),
            CategoryData(Icons.Default.Coffee, "Bebidas"),
            CategoryData(Icons.Default.Icecream, "Helados")
        )
    }
}