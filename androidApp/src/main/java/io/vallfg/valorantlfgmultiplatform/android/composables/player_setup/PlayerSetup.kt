package io.vallfg.valorantlfgmultiplatform.android.composables.player_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.android.R
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.domain.usecase.ParseError
import io.vallfg.valorantlfgmultiplatform.screen_models.player_setup.PlayerSetupState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerSetup(
    state: PlayerSetupState,
    errors: Flow<String>,
    onPlayerAccountChange: (account: String) -> Unit,
    onGetPlayerButtonClick: (account: String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        errors.collect { err ->
            snackbarHostState.showSnackbar(
                message = err,
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }
    }

    LfgScaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            PlayerTextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                text = state.account,
                onTextChanged = onPlayerAccountChange,
                playerTextFieldState = rememberPlayerTextFieldState(
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onGetPlayerButtonClick(state.account)
                        }
                    ),
                    searching = state.fetching
                )
            )
            Button(
                onClick = { onGetPlayerButtonClick(state.account) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red.copy(alpha = 0.7f)
                ),
                enabled = state.parsingErrors.isEmpty() && state.account.isNotBlank()
            ) {
                Text(text = "Next")
            }
            LazyColumn {
                items(state.parsingErrors) { err ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 32.dp,
                                end = 32.dp,
                                top = 32.dp
                            ),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = stringResource(
                                id = when(err) {
                                    ParseError.Empty -> R.string.parse_error_blank
                                    ParseError.MissingTag -> R.string.parse_error_tag
                                }
                            ),
                            color = Color.Red,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
