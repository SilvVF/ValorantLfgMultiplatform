package io.vallfg.valorantlfgmultiplatform.android.composables.post_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround

data class FilterChipItem(
    val selected: Boolean,
    val title: String,
    val selectedItem: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTopBar(
    appliedFilters: Int,
    filters: List<FilterChipItem>,
    onFilterClicked: (filterChipItem: FilterChipItem) -> Unit = {},
    onRemoveFilters: () -> Unit = {}
) {

    val clearChipVisible by remember(filters) {
        derivedStateOf { filters.any { it.selected } }
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBackGround
        ),
        title = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                AnimatedVisibility(visible = clearChipVisible) {
                    FilterCountAssistChip(
                        filterCount = appliedFilters,
                    ) { onRemoveFilters() }
                }
                Spacer(modifier = Modifier.width(8.dp))
                LazyRow {
                    items(
                        items = filters,
                        key = { it.title }
                    ) {filter ->
                        FilterChip(
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = BluishGray,
                                selectedContainerColor = Color.Red
                            ),
                            shape = RoundedCornerShape(100),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = BluishGray,
                                selectedBorderColor = Color.Cyan
                            ),
                            selected = filter.selected,
                            onClick = { onFilterClicked(filter) },
                            label = {
                                if (filter.selected) {
                                    LfgText(filter.selectedItem)
                                } else {
                                    LfgText(text = filter.title)
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = ""
                                )
                            },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        }
    )
}
