package com.llego.multiplatform.ui.state

import com.llego.multiplatform.data.model.HomeData
import com.llego.multiplatform.data.model.Product
import com.llego.multiplatform.data.model.Store
import com.llego.multiplatform.ui.components.organisms.CategoryData

/**
 * Represents the complete state of the Home Screen with unified loading state
 */
data class HomeScreenState(
    val homeDataState: UiState<HomeData> = UiState.Loading,
    val categoriesState: UiState<List<CategoryData>> = UiState.Loading,
    val searchQuery: String = "",
    val selectedCategoryIndex: Int = 0,
    val productCounts: Map<Int, Int> = emptyMap()
) {
    /**
     * Returns the list of products if successfully loaded, empty list otherwise
     */
    val products: List<Product>
        get() = homeDataState.getDataOrNull()?.products ?: emptyList()
    
    /**
     * Returns the list of categories if successfully loaded, empty list otherwise
     */
    val categories: List<CategoryData>
        get() = categoriesState.getDataOrNull() ?: emptyList()
    
    /**
     * Returns the list of stores if successfully loaded, empty list otherwise
     */
    val stores: List<Store>
        get() = homeDataState.getDataOrNull()?.stores ?: emptyList()
    
    /**
     * Returns true if any data is currently loading
     */
    val isLoading: Boolean
        get() = homeDataState.isLoading() || categoriesState.isLoading()
    
    /**
     * Returns true if there's any error in loading data
     */
    val hasError: Boolean
        get() = homeDataState.isError() || categoriesState.isError()
    
    /**
     * Returns the total number of items in cart
     */
    val totalCartItems: Int
        get() = productCounts.values.sum()
    
    /**
     * Returns filtered products based on search query and selected category
     */
    val filteredProducts: List<Product>
        get() {
            val baseProducts = products
            
            return baseProducts.filter { product ->
                // Filter by search query if provided
                val matchesSearch = if (searchQuery.isBlank()) {
                    true
                } else {
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.shop.contains(searchQuery, ignoreCase = true)
                }
                
                matchesSearch
            }
        }
}

/**
 * Represents user events/actions on the Home Screen
 */
sealed interface HomeScreenEvent {
    /**
     * User wants to refresh/reload data
     */
    data object Refresh : HomeScreenEvent
    
    /**
     * User updated search query
     */
    data class SearchQueryChanged(val query: String) : HomeScreenEvent
    
    /**
     * User selected a category
     */
    data class CategorySelected(val categoryIndex: Int) : HomeScreenEvent
    
    /**
     * User wants to increment product count
     */
    data class IncrementProduct(val productId: Int) : HomeScreenEvent
    
    /**
     * User wants to decrement product count
     */
    data class DecrementProduct(val productId: Int) : HomeScreenEvent
    
    /**
     * User clicked on "See More" button
     */
    data object SeeMoreClicked : HomeScreenEvent
    
    /**
     * User clicked on cart button
     */
    data object CartClicked : HomeScreenEvent
    
    /**
     * User wants to retry after an error
     */
    data object RetryClicked : HomeScreenEvent
}