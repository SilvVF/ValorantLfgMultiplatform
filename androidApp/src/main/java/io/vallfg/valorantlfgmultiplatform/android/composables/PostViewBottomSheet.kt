package io.vallfg.valorantlfgmultiplatform.android.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBluishBlack
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray
import io.vallfg.valorantlfgmultiplatform.android.theme.parseColor
import io.vallfg.valorantlfgmultiplatform.getColorHex
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PostViewBottomSheet(
    state: PostViewState.Success,
    currentBottomSheetContentKey: String?,
    hideSheet: () -> Unit,
    onFilterClick: (filter: Filterable) -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var searching by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth()
            .background(BluishGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(0.2f)
                .height(3.dp)
                .clip(RoundedCornerShape(100))
                .background(DarkBackGround)
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
        AnimatedContent(
            targetState = currentBottomSheetContentKey,
            transitionSpec = {
                val targetIndex = state.filters.keys.indexOf(targetState)
                val currentIndex = state.filters.keys.indexOf(initialState)

                if (targetIndex - currentIndex >= 0) {
                    slideInHorizontally { it } with slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } with slideOutHorizontally { it }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { contentKey ->
            contentKey?.let { key ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top
                ) {
                    AnimatedContent(
                        targetState = searching
                    ) { targetValue ->
                        if (!targetValue) {
                           FilterableNameBar(
                               modifier = Modifier.fillMaxWidth(),
                               key = key,
                               onSearchClick = {
                                    searching = true
                               },
                               hideSheet = hideSheet
                           )
                        } else {
                            FilterSearchBar(
                                searchText = searchText,
                                onCloseSearchClick = {
                                    searchText = ""
                                    searching = false
                                },
                                onSearchTextChange = {
                                    searchText = it
                                }
                            )
                        }
                    }
                    state.filters[key]?.let { filterables ->

                        val filterItems by remember(filterables, searchText) {
                            derivedStateOf {
                                searchText.ifBlank {
                                    return@derivedStateOf filterables
                                }
                                filterables.filter { searchText.lowercase() in it.string.lowercase() }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            contentPadding = WindowInsets.systemBars.asPaddingValues()
                        ) {
                            if (key == "Sort by") {
                                items(filterItems.chunked(2)) { filters ->
                                    if (filters.size != 2) {
                                        return@items
                                    }
                                    SortGroupItem(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        onFilterClick = onFilterClick,
                                        sortByAscending = filters.first(),
                                        sortByDescending = filters.last(),
                                        selectedAscending = filters.first() in state.appliedFilters,
                                        selectedDescending = filters.last() in state.appliedFilters
                                    )
                                }
                            } else {
                                items(filterItems) {
                                    FilterItem(
                                        filter = it,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onFilterClick(it)
                                            },
                                        selected = it in state.appliedFilters
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterSearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onCloseSearchClick: () -> Unit,
    onSearchTextChange: (String) -> Unit
) {
    Row(modifier) {
        IconButton(
            onClick = onCloseSearchClick,
            modifier = Modifier.offset(y = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "close",
                tint = Color.White
            )
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = LightGray,
                unfocusedIndicatorColor = LightGray,
                cursorColor = Color.Red
            ),
            value = searchText,
            onValueChange = onSearchTextChange,
            singleLine = true,
            placeholder = {
                LfgText("Search Filters")
            },
            trailingIcon = {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear"
                    )
                }
            }
        )
    }
}

@Composable
fun SortGroupItem(
    modifier: Modifier = Modifier,
    sortByAscending: Filterable,
    sortByDescending: Filterable,
    selectedAscending: Boolean,
    selectedDescending: Boolean,
    onFilterClick: (filter: Filterable) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        SortItem(
            sortBy = sortByAscending,
            selected = selectedAscending,
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            onFilterClick(it)
        }
        SortItem(
            sortBy = sortByDescending,
            selected = selectedDescending,
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            onFilterClick(it)
        }
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(12.dp)
        .background(DarkBackGround))
}

@Composable
fun SortItem(
    modifier: Modifier = Modifier,
    sortBy: Filterable,
    selected: Boolean,
    onFilterClick: (filter: Filterable) -> Unit
) {
    Row(
        modifier = modifier.clickable { onFilterClick(sortBy) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LfgText(
            text = sortBy.string,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 12.dp)
        )
        RadioButton(
            selected = selected,
            enabled = true,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Red
            ),
            modifier = Modifier.padding(end = 22.dp)
        )
    }
}

@Composable
fun FilterableNameBar(
    modifier: Modifier = Modifier,
    key: String,
    onSearchClick: () -> Unit,
    hideSheet: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = hideSheet,
                modifier = Modifier
                    .offset(y = 2.dp)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "close",
                    tint = Color.White
                )
            }
            LfgText(
                text = "Filter by $key",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier
                .offset(y = 2.dp)
                .padding(end = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "close",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FilterItem(
    modifier: Modifier = Modifier,
    filter: Filterable,
    selected: Boolean
) {
    val color by remember {
        derivedStateOf {
            parseColor("#${filter.getColorHex()}")
        }
    }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        Color(color)
                    )
            )
            Spacer(modifier = Modifier.width(32.dp))
            LfgText(text = filter.string, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(62.dp))
        }
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Selected",
                tint = Color.Red,
                modifier = Modifier.padding(end = 22.dp)
            )
        }
    }
}