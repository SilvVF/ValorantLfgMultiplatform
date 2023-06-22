package io.vallfg.valorantlfgmultiplatform.android.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.LocalTheme

@Composable
fun LfgBackgroundBox(
    modifier: Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable() (BoxScope.() -> Unit)
) {
    val theme = LocalTheme.current

    val containerColor = if(theme.isDarkTheme) {
        DarkBackGround
    } else {
        LightBluishGray
    }

    Box(modifier.background(containerColor), contentAlignment = contentAlignment) {
        content()
    }
}