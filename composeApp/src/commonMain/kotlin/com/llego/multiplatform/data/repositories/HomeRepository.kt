package com.llego.multiplatform.data.repositories

import com.llego.multiplatform.data.model.HomeData
import com.llego.multiplatform.data.model.Product
import com.llego.multiplatform.data.model.Store
import com.llego.multiplatform.data.network.GraphQLClient
import com.llego.multiplatform.graphql.GetHomeDataQuery

class HomeRepository {
    
    private val apolloClient = GraphQLClient.apolloClient
    
    suspend fun getHomeData(): HomeData {
        val response = apolloClient.query(GetHomeDataQuery()).execute()
        
        if (response.hasErrors()) {
            throw Exception("GraphQL errors: ${response.errors?.joinToString { it.message }}")
        }
        
        val data = response.data ?: throw Exception("No data received from GraphQL server")
        
        val products = data.products.map { productData ->
            Product(
                id = productData.id,
                name = productData.name,
                shop = productData.shop,
                weight = productData.weight,
                price = productData.price,
                imageUrl = productData.imageUrl
            )
        }
        
        val stores = data.stores.map { storeData ->
            Store(
                id = storeData.id,
                name = storeData.name,
                etaMinutes = storeData.etaMinutes,
                logoUrl = storeData.logoUrl,
                bannerUrl = storeData.bannerUrl
            )
        }
        
        return HomeData(
            products = products,
            stores = stores
        )
    }
}