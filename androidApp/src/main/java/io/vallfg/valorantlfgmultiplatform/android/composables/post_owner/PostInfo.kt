package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.atoms.TopAppBarStyle
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostState

@Composable
fun PostInfo(
    post: PostState,
    postId: String,
) {

    LfgScaffold(
        topAppBarStyle = TopAppBarStyle(
            title = {
                LfgText(text = postId)
            }
        )
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

        }
    }
}