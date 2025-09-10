package com.llego.multiplatform.data.repositories

import com.llego.multiplatform.data.model.Product

class ProductRepository {
    
    fun getProducts(): List<Product> {
        return listOf(
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
}