package com.example.musicapp.domain.logic.impure.iface.storage.l1.write

interface ThumbnailStorageWrite {
    fun saveOrJustGetExistingRelativePath(data: ByteArray): String
}