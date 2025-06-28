package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMapping
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongAdd
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongThumbnailUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.SongFileDbAdd
import java.io.File
import javax.inject.Inject

class SongAddImpl @Inject constructor(
    private val filePut: SongFileDbAdd,
    private val metaParser: MetaParser,
    private val metaKeyMapping: MetaKeyMapping,
    private val metaDbUpdate: MetaDbUpdate,
    private val thumbnailStorageEdit: SongThumbnailUpdate,
) : SongAdd {
    override fun addNewIfNotExists(file: File): SongId? {
        val id = filePut.putNewIfNotExists(file) ?: return null
        val meta = metaParser.getFullMetaFromFile(file, metaKeyMapping.getMetaKeyMappings())
        metaDbUpdate.updateMetadata(id, meta.properties)
        meta.icon?.let {
            thumbnailStorageEdit.saveIcon(id, it)
        }
        return id
    }
}