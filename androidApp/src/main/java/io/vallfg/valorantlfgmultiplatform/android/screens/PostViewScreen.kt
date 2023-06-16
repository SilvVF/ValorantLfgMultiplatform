@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package io.vallfg.valorantlfgmultiplatform.android.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgBottomSheetScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgButton
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.composables.FilterCountAssistChip
import io.vallfg.valorantlfgmultiplatform.android.composables.PostViewBottomSheet
import io.vallfg.valorantlfgmultiplatform.android.composables.PostViewFloatingButtons
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.ZoneOffset
import kotlinx.datetime.toInstant

data class


PostViewScreenProps(
    val name: String,
    val tag: String
)

class PostViewScreen(
    private val props: PostViewScreenProps,
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostViewScreenModel>()

        val state = screenModel.state.collectAsState().value
        
        when (state) {
            is PostViewState.Success -> {
                PostView(
                    state = state,
                    applyFilter = { filter: Filterable ->
                        screenModel.applyFilter(filter)
                    },
                    removeAllFilters = {
                        screenModel.clearFilters()
                    }
                )
            }
            else -> {
                Surface(Modifier.fillMaxSize()) {

                }
            }
        }
    }
}

@Composable
fun PostView(
    state: PostViewState.Success,
    removeAllFilters: () -> Unit,
    applyFilter: (filter: Filterable) -> Unit
) {

    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
    )
    val sheetState = sheetScaffoldState.bottomSheetState
    val scope = rememberCoroutineScope()

    var currentBottomSheetContentKey by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = sheetState.currentValue) {
        when(sheetState.currentValue) {
            SheetValue.Hidden -> {
                currentBottomSheetContentKey = null
            }
            else -> Unit
        }
    }

    LaunchedEffect(currentBottomSheetContentKey) {
        if (currentBottomSheetContentKey != null) {
            sheetState.expand()
        } else {
            sheetState.hide()
        }
    }

    val rankListState = rememberLazyListState()

    val scrolledPastThreshold by remember {
        derivedStateOf { rankListState.firstVisibleItemIndex > 5 }
    }


  PostViewFloatingButtons(
      sheetVisible = sheetState.isVisible,
      showScrollToTop = scrolledPastThreshold,
      onScrollToTopClick = {
          scope.launch {
              rankListState.animateScrollToItem(0)
          }
      },
      onCreatePostClick = {

      }
  )


    LfgBottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
              PostViewBottomSheet(
                  state = state,
                  currentBottomSheetContentKey = currentBottomSheetContentKey,
                  hideSheet = {
                      scope.launch {
                          sheetState.hide()
                      }
                  },
                  onFilterClick = applyFilter
              )
        },
        scaffoldState = sheetScaffoldState,
        sheetDragHandle = {},
        sheetContainerColor = BluishGray,
        sheetShape = RoundedCornerShape(12.dp),
        topBar = {
            FilterTopBar(
                filters = remember(state.filters, state.appliedFilters) {
                    derivedStateOf {
                        state.filters.map { (name, items) ->
                            FilterChipItem(
                                selected = state.appliedFilters.any { it in items },
                                title = name,
                                selectedItem = state.appliedFilters.find { it in items }?.string ?: ""
                            )
                        }
                    }.value
                },
                appliedFilters = state.appliedFilters.size,
                onFilterClicked = {
                    currentBottomSheetContentKey = it.title
                },
                onRemoveFilters = removeAllFilters
            )
        },
    ) { _ ->
        LazyColumn(
            state = rankListState,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    ScaffoldDefaults.contentWindowInsets
                        .exclude(WindowInsets.systemBars)
                )
        ) {
            items(
                items = state.posts,
                key = { post -> post.id },
            ) {
                PostCard(
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

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: Post,
) {
    val ctx = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(ctx)
                .data(post.minRankIconUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = CenterVertically) {
                LfgText(
                    text = "Min Rank |",
                    color = LightGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 2.dp)
                )
                LfgText(
                    text = post.minRank.string,
                    fontWeight = FontWeight.Bold
                )
            }
            Row {
                for (i in 0..post.needed) {
                    PlayerIcon(
                        playerName = post.players.getOrNull(i)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            LfgText(
                text = post.gameMode.string,
                fontWeight = FontWeight.SemiBold
            )
        }

        val timeSincePost by remember {
            derivedStateOf {
                val now = Clock.System.now().epochSeconds
                val postTimeEpochSeconds = post.date.toInstant(TimeZone.currentSystemDefault()).epochSeconds
                val minutesSince = (now - postTimeEpochSeconds) / 60
                if (minutesSince < 1) {
                    "<1"
                } else {
                    "${minutesSince.toInt()}m"
                }
            }
        }
        Box(Modifier
            .weight(1f)
            .fillMaxHeight(),
            contentAlignment = TopEnd
        ) {
            LfgText(
                text = timeSincePost,
                color = LightGray,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun PlayerIcon(
    playerName: List<String>?
) {
    if (playerName != null) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Red)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Player",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(LightGray)
                .size(24.dp)
        )
    }
}