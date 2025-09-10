package com.llego.multiplatform.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.llego.multiplatform.data.model.Store
import com.llego.multiplatform.ui.components.molecules.StoreCard

@Composable
fun StoresSection(
    stores: List<Store>,
    onSeeMoreClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Section(
        title = "Tiendas cerca de ti",
        onSeeMoreClick = onSeeMoreClick,
        modifier = modifier
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stores) { store ->
                StoreCard(
                    storeName = store.name,
                    etaMinutes = store.etaMinutes,
                    logoUrl = store.logoUrl,
                    bannerUrl = store.bannerUrl
                )
            }
        }
    }
}