package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

interface MetaStorage {
    fun getAllFields(): List<Triple<SongId, MetaKey, String>>
    fun getAllFields(song: SongId): List<Pair<MetaKey, String>>
    fun getAllFields(songs: List<SongId>): List<Triple<SongId, MetaKey, String>>
    fun getFields(fields: Set<MetaKey>): List<Triple<SongId, MetaKey, String>>
    fun getFields(song: SongId, fields: Set<MetaKey>): List<Triple<SongId, MetaKey, String>>
    fun getFields(songs: List<SongId>, fields: Set<MetaKey>): List<Triple<SongId, MetaKey, String>>
}
