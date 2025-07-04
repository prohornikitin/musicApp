package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

interface FormattedMetaStorage {
    fun getAllFields(): Map<SongId, Map<MetaKey, String>>
    fun getAllFields(song: SongId): Map<MetaKey, String>
    fun getAllFields(songs: List<SongId>): Map<SongId, Map<MetaKey, String>>
    fun getFields(fields: Set<MetaKey>): Map<SongId, Map<MetaKey, String>>
    fun getFields(song: SongId, fields: Set<MetaKey>): Map<MetaKey, String>
    fun getFields(songs: List<SongId>, fields: Set<MetaKey>): Map<SongId, Map<MetaKey, String>>
}