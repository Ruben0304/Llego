package com.llego.multiplatform.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llego.multiplatform.data.repositories.HomeRepository
import com.llego.multiplatform.data.repositories.CategoryRepository
import com.llego.multiplatform.data.model.Product
import com.llego.multiplatform.data.model.Store
import com.llego.multiplatform.data.model.HomeData
import com.llego.multiplatform.ui.components.organisms.CategoryData
import com.llego.multiplatform.ui.state.HomeScreenState
import com.llego.multiplatform.ui.state.HomeScreenEvent
import com.llego.multiplatform.ui.state.UiState
import com.llego.multiplatform.ui.state.isError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class HomeViewModel(
    private val homeRepository: HomeRepository = HomeRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        loadData()
    }

    /**
     * Handle user events
     */
    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.Refresh -> loadData()
            is HomeScreenEvent.SearchQueryChanged -> updateSearchQuery(event.query)
            is HomeScreenEvent.CategorySelected -> selectCategory(event.categoryIndex)
            is HomeScreenEvent.IncrementProduct -> incrementProduct(event.productId)
            is HomeScreenEvent.DecrementProduct -> decrementProduct(event.productId)
            is HomeScreenEvent.SeeMoreClicked -> handleSeeMoreClick()
            is HomeScreenEvent.CartClicked -> handleCartClick()
            is HomeScreenEvent.RetryClicked -> retryLoadingData()
        }
    }

    /**
     * Load initial data (home data and categories) concurrently
     */
    private fun loadData() {
        viewModelScope.launch {
            // Set all states to loading immediately
            _state.update { 
                it.copy(
                    homeDataState = UiState.Loading,
                    categoriesState = UiState.Loading
                )
            }
            
            // Load all data sets concurrently
            val homeDataDeferred = async { loadHomeData() }
            val categoriesDeferred = async { loadCategoriesData() }
            
            // Wait for home data (products and stores) and update state
            try {
                val homeData = homeDataDeferred.await()
                val productCounts = homeData.products.associate { it.id to 0 }
                _state.update { 
                    it.copy(
                        homeDataState = UiState.Success(homeData),
                        productCounts = productCounts
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        homeDataState = UiState.Error(e, "Failed to load home data")
                    )
                }
            }
            
            // Wait for categories and update state
            try {
                val categories = categoriesDeferred.await()
                _state.update { 
                    it.copy(categoriesState = UiState.Success(categories))
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        categoriesState = UiState.Error(e, "Failed to load categories")
                    )
                }
            }
        }
    }

    /**
     * Load home data (products and stores) from unified repository
     */
    private suspend fun loadHomeData(): HomeData {
        return homeRepository.getHomeData()
    }
    
    /**
     * Load categories data (suspending function for concurrent loading)
     */
    private suspend fun loadCategoriesData(): List<CategoryData> {
        return categoryRepository.getCategories()
    }
    




    /**
     * Update search query
     */
    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    /**
     * Select a category
     */
    private fun selectCategory(categoryIndex: Int) {
        _state.update { it.copy(selectedCategoryIndex = categoryIndex) }
    }

    /**
     * Increment product count
     */
    private fun incrementProduct(productId: Int) {
        _state.update { currentState ->
            val currentCount = currentState.productCounts[productId] ?: 0
            val updatedCounts = currentState.productCounts.toMutableMap()
            updatedCounts[productId] = currentCount + 1
            
            currentState.copy(productCounts = updatedCounts)
        }
    }

    /**
     * Decrement product count
     */
    private fun decrementProduct(productId: Int) {
        _state.update { currentState ->
            val currentCount = currentState.productCounts[productId] ?: 0
            if (currentCount > 0) {
                val updatedCounts = currentState.productCounts.toMutableMap()
                updatedCounts[productId] = currentCount - 1
                currentState.copy(productCounts = updatedCounts)
            } else {
                currentState
            }
        }
    }

    /**
     * Handle "See More" button click
     */
    private fun handleSeeMoreClick() {
        // TODO: Implement navigation to full product list
        // For now, just log or show a message
        println("See More clicked - Navigate to full product list")
    }

    /**
     * Handle cart button click
     */
    private fun handleCartClick() {
        // TODO: Implement navigation to cart screen
        // For now, just log or show cart summary
        val totalItems = _state.value.totalCartItems
        println("Cart clicked - Total items: $totalItems")
    }

    /**
     * Retry loading data after error
     */
    private fun retryLoadingData() {
        loadData()
    }

    /**
     * Get product count for a specific product
     */
    fun getProductCount(productId: Int): Int {
        return _state.value.productCounts[productId] ?: 0
    }

    /**
     * Check if a product has items in cart
     */
    fun hasProductInCart(productId: Int): Boolean {
        return getProductCount(productId) > 0
    }
}