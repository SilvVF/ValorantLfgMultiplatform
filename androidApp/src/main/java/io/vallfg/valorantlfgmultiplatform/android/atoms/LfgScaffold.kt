package io.vallfg.valorantlfgmultiplatform.android.atoms

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBluishBlack
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.LocalTheme


data class TopAppBarStyle @OptIn(ExperimentalMaterial3Api::class) constructor(
    val title: @Composable () -> Unit,
    val modifier: Modifier = Modifier,
    val navigationIcon: @Composable () -> Unit = {},
    val actions: @Composable RowScope.() -> Unit = {},
    val scrollBehavior: TopAppBarScrollBehavior? = null
)

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
                             containerColor = containerColor
                         ),
                         scrollBehavior = it.scrollBehavior
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