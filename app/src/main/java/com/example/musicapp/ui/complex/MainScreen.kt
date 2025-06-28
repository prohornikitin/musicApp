package com.example.musicapp.ui.complex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.config.SongCardStyle
import com.example.config.SongCardStyle.FontConfig
import com.example.musicapp.R
import com.example.musicapp.ui.reusable.SearchBar
import com.example.musicapp.ui.reusable.songcard.SongItem
import com.example.musicapp.uistate.MainVm
import com.example.musicapp.uistate.PlayerVm
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: MainVm, playerVm: PlayerVm, openDrawer: () -> Unit) {
    val songCardStyle =
        SongCardStyle(mainFont = FontConfig(sizeSp = 20), bottomFont = FontConfig(sizeSp = 20))
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row {
                SearchBar(
                    vm.searchQuery,
                    onTextChanged = vm::onSearchQueryChanged,
                    modifier = Modifier.fillMaxWidth(1f),
                    leadingIcon = {
                        IconButton(
                            onClick = openDrawer
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.open_menu),
                            )
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            PullToRefreshBox(
                modifier = Modifier.weight(1f),
                isRefreshing = vm.isLoading,
                onRefresh = vm::reload,
            ) {
                LazyColumn(Modifier) {
                    items(
                        items = vm.afterSearch,
                        key = { it.id.raw },
                    ) { song ->
                        SongItem(
                            data = song,
                            style = songCardStyle,
                            onOptionClick = {
                                vm.execSongOption(song.id, it)
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(onClick = {
                                    playerVm.changePlaylist(vm.getPlaylistFrom(song.id))
                                })
                        )
                    }
                }
            }
            if (playerVm.currentSong != null) {
                PlayerCard(
                    playerVm.currentSong!!,
                    Modifier
                        .wrapContentHeight(Alignment.Bottom, unbounded = true)
                        .fillMaxWidth(),
                    songCardStyle,
                    playerVm.isPlaying,
                    onPlayClicked = playerVm::togglePause,
                )
            }
        }
    }
}