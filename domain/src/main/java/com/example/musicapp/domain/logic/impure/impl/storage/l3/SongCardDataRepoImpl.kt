package com.example.musicapp.domain.logic.impure.impl.storage.l3

import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.ObservableSongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.SongCardDataRead
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongCardDataRepoImpl(
    private val textStorage: ObservableSongCardTextStorage,
    private val thumbnailStorage: SongThumbnailStorage,
) : SongCardDataRead, ObservableSongCardTextStorage.Listener {
    init {
        textStorage.addListener(this)
    }

    val flowScope = CoroutineScope(Dispatchers.Default)

    override fun onCardDataChanged(id: SongId, text: SongCardText) {
        flowScope.launch {
            flowBySong[id]?.emit(SongCardData(
                id = id,
                mainText = text.main,
                bottomText = text.sub,
                iconBitmap = thumbnailStorage.get(id)
            ))
        }
    }

    private val flowBySong: MutableMap<SongId, MutableStateFlow<SongCardData>> = mutableMapOf()

    override fun getAsFlow(id: SongId): StateFlow<SongCardData> {
        val oldFlow = flowBySong[id]
        if(oldFlow != null) {
            return oldFlow
        }
        return MutableStateFlow(get(id)).also {
            flowBySong[id] = it
        }
    }

    override fun get(id: SongId): SongCardData {
        val text = textStorage.get(id)
        SongCardData(
            id = id,
            mainText = text.main,
            bottomText = text.sub,
            iconBitmap = thumbnailStorage.get(id)
        )

    }
}