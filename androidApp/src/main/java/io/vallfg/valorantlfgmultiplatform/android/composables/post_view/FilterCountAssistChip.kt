package io.vallfg.valorantlfgmultiplatform.android.composables.post_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBluishBlack

private val TriangleShape = GenericShape { size, _ ->
    // 1)
    moveTo(size.width / 2f, size.height)

    // 2)
    lineTo(size.width, 0f)

    // 3)
    lineTo(0f, 0f)
}

@Composable
fun FilterCountAssistChip(
    modifier: Modifier = Modifier,
    filterCount: Int,
    onRemoveFilters: () -> Unit,
) {

    var dropdownVisible by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        AssistChip(
            colors = AssistChipDefaults.assistChipColors(
                containerColor = BluishGray,
            ),
            border = AssistChipDefaults.assistChipBorder(
                borderColor = BluishGray
            ),
            shape = RoundedCornerShape(100),
            onClick = { dropdownVisible = !dropdownVisible },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.clip(TriangleShape)
                )
            },
            label = {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    LfgText(text = filterCount.toString(), color = DarkBluishBlack)
                }
            }
        )
        DropdownMenu(
            expanded = dropdownVisible,
            onDismissRequest = { dropdownVisible = !dropdownVisible },
            modifier = Modifier.background(BluishGray)
        ) {
            DropdownMenuItem(
                onClick = {
                    onRemoveFilters()
                    dropdownVisible = !dropdownVisible
                },
                text = { Text(text = "clear filters") },
                colors = MenuDefaults.itemColors(
                    textColor = Color.White,
                )
            )
        }
    }
}