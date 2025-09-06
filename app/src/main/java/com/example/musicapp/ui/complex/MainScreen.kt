package com.example.musicapp.ui.complex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.musicapp.domain.data.SongCardStyle
import com.example.musicapp.domain.data.SongCardStyle.FontConfig
import com.example.musicapp.ui.reusable.MenuOpenButton
import com.example.musicapp.ui.reusable.SearchBar
import com.example.musicapp.ui.reusable.songcard.SongItem
import com.example.musicapp.uistate.MainVm
import com.example.musicapp.uistate.PlayerVm

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
                        MenuOpenButton(openDrawer)
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
                onRefresh = vm::refresh,
            ) {
                LazyColumn(Modifier) {
                    items(
                        items = vm.songs,
                        key = { it.raw },
                    ) { song ->
                        SongItem(
                            data = vm.loadCard(song).collectAsStateWithLifecycle().value,
                            style = songCardStyle,
                            onOptionClick = {
                                vm.execSongOption(song, it)
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(onClick = {
                                    playerVm.changePlaylist(vm.getPlaylistFrom(song))
                                })
                        )
                    }
                }
            }

            val currentSongCard by playerVm.currentSongCard.collectAsStateWithLifecycle()
            if (currentSongCard != null) {
                PlayerCard(
                    currentSongCard!!,
                    Modifier
                        .wrapContentHeight(Alignment.Bottom, unbounded = true)
                        .fillMaxWidth(),
                    songCardStyle,
                    playerVm.isPlaying.collectAsStateWithLifecycle().value,
                    onPlayClicked = playerVm::togglePlayPause,
                )
            }
        }
    }
}