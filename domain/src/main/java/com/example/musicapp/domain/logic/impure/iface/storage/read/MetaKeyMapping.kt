package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.musicapp.domain.data.MetaKey

fun interface MetaKeyMapping {
    fun getMetaKeyMappings(): Map<String, MetaKey>
}