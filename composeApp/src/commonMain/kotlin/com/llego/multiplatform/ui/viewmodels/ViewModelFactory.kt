package com.llego.multiplatform.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.llego.multiplatform.data.repositories.ProductRepository
import com.llego.multiplatform.data.repositories.CategoryRepository
import kotlin.reflect.KClass

/**
 * Factory for creating ViewModels with dependency injection
 */
class ViewModelFactory(
    private val productRepository: ProductRepository = ProductRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T {
        return when (modelClass) {
            HomeViewModel::class -> HomeViewModel(
                productRepository = productRepository,
                categoryRepository = categoryRepository
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
        }
    }
}

/**
 * Helper function to create a ViewModelFactory with default dependencies
 */
fun createViewModelFactory(
    productRepository: ProductRepository = ProductRepository(),
    categoryRepository: CategoryRepository = CategoryRepository()
): ViewModelFactory {
    return ViewModelFactory(
        productRepository = productRepository,
        categoryRepository = categoryRepository
    )
}