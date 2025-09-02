package com.example.musicapp.ui.complex

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.config.SongCardStyle
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.ui.preview.SongCardDataProvider
import com.example.musicapp.ui.reusable.songcard.SongCard

@Preview
@Composable
fun PlayerCard(
    @PreviewParameter(SongCardDataProvider::class) data: SongCardData,
    modifier: Modifier = Modifier,
    style: SongCardStyle = SongCardStyle(),
    playing: Boolean = false,
    onPlayClicked: () -> Unit = {},
    elevation: CardElevation = CardDefaults.cardElevation(),
) {
    Card(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        elevation = elevation
    ) {
        Row {
            SongCard(data, Modifier.weight(1f), style)
            IconButton(onClick = onPlayClicked) {
                Icon(
                    imageVector = if(playing) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = null,
                )
            }
        }
    }
}