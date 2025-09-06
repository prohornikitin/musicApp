package com.example.musicapp.ui.reusable.songcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.musicapp.domain.data.SongCardStyle
import com.example.musicapp.R
import com.example.musicapp.data.SongOption
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.ui.preview.SongCardDataProvider
import com.example.musicapp.ui.reusable.MyDropdownMenu

@Preview
@Composable
fun SongItem(
    @PreviewParameter(SongCardDataProvider::class) data: SongCardData,
    modifier: Modifier = Modifier,
    onOptionClick: (SongOption) -> Unit = {},
    style: SongCardStyle = SongCardStyle(),
    elevation: CardElevation = CardDefaults.cardElevation(),
) {
    var optionsMenuOpen by remember { mutableStateOf(false) }
    Card(
        modifier.background(MaterialTheme.colorScheme.background).wrapContentHeight(),
        elevation = elevation,
    ) {
        Row(Modifier.background(Color.Transparent)) {
            SongCard(
                data,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                style,
            )
            IconButton(
                modifier = Modifier.fillMaxHeight(),
                onClick = { optionsMenuOpen = true }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.options),
                )
                MyDropdownMenu(
                    expanded = optionsMenuOpen,
                    items = SongOption.entries,
                    getTitle = { it.title() },
                    onClick = onOptionClick,
                    onDismiss = { optionsMenuOpen = false }
                )
            }
        }
    }
}