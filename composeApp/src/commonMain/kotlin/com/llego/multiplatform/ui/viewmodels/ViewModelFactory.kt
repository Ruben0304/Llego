package com.llego.multiplatform.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.llego.multiplatform.data.repositories.HomeRepository
import com.llego.multiplatform.data.repositories.CategoryRepository
import kotlin.reflect.KClass

/**
 * Factory for creating ViewModels with dependency injection
 */
class ViewModelFactory(
    private val homeRepository: HomeRepository = HomeRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras
    ): T {
        return when (modelClass) {
            HomeViewModel::class -> HomeViewModel(
                homeRepository = homeRepository,
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
    homeRepository: HomeRepository = HomeRepository(),
    categoryRepository: CategoryRepository = CategoryRepository()
): ViewModelFactory {
    return ViewModelFactory(
        homeRepository = homeRepository,
        categoryRepository = categoryRepository
    )
}