package com.llego.multiplatform.data.repositories

import com.llego.multiplatform.data.model.Store

class StoreRepository {
    
    fun getStores(): List<Store> {
        return listOf(
            Store(
                id = 1,
                name = "T&T Food Market",
                etaMinutes = 12,
                logoUrl = "https://images.unsplash.com/photo-1606813907291-fc2d69fce182?w=200&h=200&fit=crop",
                bannerUrl = "https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=800&h=400&fit=crop"
            ),
            Store(
                id = 2,
                name = "Fresh Market",
                etaMinutes = 8,
                logoUrl = "https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=200&h=200&fit=crop",
                bannerUrl = "https://images.unsplash.com/photo-1542838132-92c53300491e?w=800&h=400&fit=crop"
            ),
            Store(
                id = 3,
                name = "Organic Valley",
                etaMinutes = 15,
                logoUrl = "https://images.unsplash.com/photo-1607631568010-a87245c0daf8?w=200&h=200&fit=crop",
                bannerUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=400&fit=crop"
            ),
            Store(
                id = 4,
                name = "Green Corner",
                etaMinutes = 20,
                logoUrl = "https://images.unsplash.com/photo-1599481238505-461a2d3d2b58?w=200&h=200&fit=crop",
                bannerUrl = "https://images.unsplash.com/photo-1555992336-03a23c0b3e0e?w=800&h=400&fit=crop"
            ),
            Store(
                id = 5,
                name = "Local Deli",
                etaMinutes = 6,
                logoUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=200&h=200&fit=crop",
                bannerUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&h=400&fit=crop"
            )
        )
    }
}