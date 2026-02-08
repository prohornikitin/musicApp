package afc.musicapp.ui.complex

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import afc.musicapp.data.SongOption
import afc.musicapp.domain.entities.SongCardStyle
import afc.musicapp.domain.entities.SongCardStyle.FontConfig
import afc.musicapp.ui.nav.Route
import afc.musicapp.ui.reusable.MenuOpenButton
import afc.musicapp.ui.reusable.SearchBar
import afc.musicapp.ui.reusable.songcard.SongItem
import afc.musicapp.uistate.vm.MainVm
import afc.musicapp.uistate.vm.PlayerVm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: MainVm, playerVm: PlayerVm, openDrawer: () -> Unit, nav: NavController) {
    val songCardStyle = SongCardStyle(mainFont = FontConfig(sizeSp = 20), bottomFont = FontConfig(sizeSp = 20))
    Scaffold(
        topBar = {
            Row {
                SearchBar(
                    vm.searchQuery,
                    onTextChanged = vm::onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .background(MaterialTheme.colorScheme.background),
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
                    itemsIndexed(
                        vm.cardsAndKeys,
                        key = { index, item -> item.first.value },
                    ) { index, item ->
                        if (vm.needLoadMore(index)) {
                            LaunchedEffect(Unit) {
                                vm.loadMoreCards(index)
                            }
                        }
                        val key = item.first
                        val data = item.second
                        SongItem(
                            data = data,
                            style = songCardStyle,
                            onOptionClick = {
                                if (key.isNotLoaded()) {
                                    return@SongItem
                                }
                                val id = key.toSongId()
                                when(it) {
                                    SongOption.EditMeta -> {
                                        nav.navigate(Route.TagEditor(id.raw))
                                    }
                                    SongOption.Details -> TODO()
                                    SongOption.Delete -> vm.delete(id)
                                    SongOption.Share -> vm.share(id)
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(onClick = {
                                    playerVm.changePlaylist(vm.getPlaylist(index), index)
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