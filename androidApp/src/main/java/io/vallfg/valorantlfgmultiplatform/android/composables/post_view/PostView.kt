@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package io.vallfg.valorantlfgmultiplatform.android.composables.post_view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgBottomSheetScaffold
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PostViewScreenState(
    private val scope: CoroutineScope,
    val scaffoldState: BottomSheetScaffoldState,
    val listState: LazyListState,
    private val bottomSheetState: SheetState = scaffoldState.bottomSheetState,
    val sheetValue: SheetValue = bottomSheetState.currentValue,
) {

    var bottomSheetKey: String? by mutableStateOf(null)

    val scrolledPastThreshold by derivedStateOf { listState.firstVisibleItemIndex > 5 }

    val bottomSheetVisible by derivedStateOf { bottomSheetState.isVisible }

    fun scrollToTopOfList() {
        scope.launch {
            listState.animateScrollToItem(0)
        }
    }

    fun hideSheet() {
        scope.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }

    fun showSheet() {
        scope.launch {
            scaffoldState.bottomSheetState.expand()
        }
    }
}

@Composable
fun rememberPostViewScreenState(
    scope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ),
    listState: LazyListState = rememberLazyListState(),
) = remember {
    PostViewScreenState(scope, scaffoldState, listState)
}


@Composable
fun PostView(
    posts: List<Post>,
    filters:  Map<String, List<Filterable>>,
    appliedFilters: List<Filterable>,
    removeAllFilters: () -> Unit,
    applyFilter: (filter: Filterable) -> Unit
) {

    val screenState = rememberPostViewScreenState()

    LaunchedEffect(key1 = screenState.sheetValue) {
        when(screenState.sheetValue) {
            SheetValue.Hidden -> {
                screenState.bottomSheetKey = null
            }
            else -> Unit
        }
    }

    LaunchedEffect(screenState.bottomSheetKey) {
        if(screenState.bottomSheetKey != null) {
            screenState.showSheet()
        }
    }

    BackHandler(
        enabled = screenState.bottomSheetVisible
    ) {
        screenState.hideSheet()
    }


    PostViewFloatingButtons(
        sheetVisible = screenState.bottomSheetVisible,
        showScrollToTop = screenState.scrolledPastThreshold,
        onScrollToTopClick = screenState::scrollToTopOfList,
        onCreatePostClick = {

        }
    )


    LfgBottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            PostViewBottomSheet(
                filters = filters,
                appliedFilters = appliedFilters,
                contentKey = screenState.bottomSheetKey,
                hideSheet = screenState::hideSheet,
                onFilterClick = applyFilter
            )
        },
        scaffoldState = screenState.scaffoldState,
        sheetDragHandle = {},
        sheetContainerColor = BluishGray,
        sheetShape = RoundedCornerShape(12.dp),
        topBar = {
            FilterTopBar(
                filters = remember(filters, appliedFilters) {
                    derivedStateOf {
                        filters.map { (name, items) ->
                            FilterChipItem(
                                selected = appliedFilters.any { it in items },
                                title = name,
                                selectedItem = appliedFilters.find { it in items }?.string ?: ""
                            )
                        }
                    }.value
                },
                appliedFilters = appliedFilters.size,
                onFilterClicked = {
                    screenState.bottomSheetKey = it.title
                },
                onRemoveFilters = removeAllFilters
            )
        },
    ) { _ ->
        LazyColumn(
            state = screenState.listState,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    ScaffoldDefaults.contentWindowInsets
                        .exclude(WindowInsets.systemBars)
                )
        ) {
            items(
                items = posts,
                key = { post -> post.id },
            ) {
                PostListItem(
                    post = it,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .animateItemPlacement()
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(BluishGray)
                )
            }
        }
    }
}