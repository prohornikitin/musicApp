package afc.musicapp.domain.entities

import kotlin.time.ComparableTimeMark

data class SyncedLyrics(val rows: List<Item>) {
    data class Item(val text: String, val start: ComparableTimeMark, val end: ComparableTimeMark)
}