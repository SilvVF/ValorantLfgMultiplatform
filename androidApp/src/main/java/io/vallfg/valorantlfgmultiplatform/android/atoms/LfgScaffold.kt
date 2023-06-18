package io.vallfg.valorantlfgmultiplatform.android.atoms

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.LocalTheme


data class TopAppBarStyle(
    val title: @Composable () -> Unit = {},
    val backgroundColor: Color? = null,
    val modifier: Modifier = Modifier,
    val navigationIcon: @Composable () -> Unit = {},
    val actions: @Composable RowScope.() -> Unit = {},
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LfgBottomSheetScaffold(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    sheetPeekHeight: Dp = BottomSheetDefaults.SheetPeekHeight,
    sheetShape: Shape = BottomSheetDefaults.ExpandedShape,
    sheetContainerColor: Color = BottomSheetDefaults.ContainerColor,
    sheetContentColor: Color = contentColorFor(sheetContainerColor),
    sheetTonalElevation: Dp = BottomSheetDefaults.Elevation,
    sheetShadowElevation: Dp = BottomSheetDefaults.Elevation,
    sheetDragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    sheetSwipeEnabled: Boolean = true,
    topBar: @Composable (() -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    content: @Composable (PaddingValues) -> Unit
) {

    val theme = LocalTheme.current

    val containerColor = if(theme.isDarkTheme) {
        DarkBackGround
    } else {
        LightBluishGray
    }

    BottomSheetScaffold(
        sheetContent,
        modifier,
        scaffoldState,
        sheetPeekHeight,
        sheetShape,
        sheetContainerColor,
        sheetContentColor,
        sheetTonalElevation,
        sheetShadowElevation,
        sheetDragHandle,
        sheetSwipeEnabled,
        topBar,
        snackbarHost,
        containerColor,
        contentColorFor(backgroundColor = containerColor)
    ) { paddingValues ->
        content(paddingValues)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LfgScaffold(
    modifier: Modifier = Modifier,
    topAppBarStyle: TopAppBarStyle? = null,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {

    val theme = LocalTheme.current

    val containerColor = if(theme.isDarkTheme) {
        DarkBackGround
    } else {
        LightBluishGray
    }

    Scaffold(
        modifier = modifier,
        topBar = {
             topAppBarStyle?.let {
                     TopAppBar(
                         title = it.title,
                         modifier = it.modifier,
                         navigationIcon = it.navigationIcon,
                         actions = it.actions,
                         windowInsets = TopAppBarDefaults.windowInsets,
                         colors = TopAppBarDefaults.topAppBarColors(
                             containerColor = topAppBarStyle.backgroundColor ?: containerColor,
                         ),
                     )
             }
        },
        bottomBar = bottomBar,
        snackbarHost= snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColorFor(backgroundColor = containerColor),
        contentWindowInsets = contentWindowInsets,
    ) { paddingValues ->
        content(paddingValues)
    }
}