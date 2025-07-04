package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.SongFileLoad
import com.example.musicapp.domain.logic.impure.iface.storage.write.MetadataEdit
import com.example.musicapp.domain.logic.impure.iface.storage.write.ThumbnailEdit
import java.io.File
import javax.inject.Inject

//class SongFileLoadImpl @Inject constructor(
//    private val metadataEdit: MetadataEdit,
//    private val metaParser: MetaParser,
//    private val thumbnailEdit: ThumbnailEdit,
//) : SongFileLoad {
//    override fun loadNewIfNotExists(file: File) {
//        if(findByFile(file) != null) {
//            return
//        }
//        val id = addSong(file)
//        reloadDataFromFile(id, file)
//    }
//
//    override fun loadNewOrUpdateData(file: File) {
//        var id = findByFile(file)
//        if(id == null) {
//            id = addSong(file)
//        }
//        reloadDataFromFile(id, file)
//    }
//
//    override fun reloadDataFromFile(id: SongId) {
//        val file = getFileById(id)
//        reloadDataFromFile(id, file)
//    }
//
//    private fun reloadDataFromFile(id: SongId, file: File) {
//        val fullMeta = metaParser.getFullMetaFromFile(file)
//        metadataEdit.update(id, fullMeta.properties)
//        metadataEdit.removeAllExceptOfAllowedKeys(id, fullMeta.properties.keys)
//        thumbnailEdit.update(id, fullMeta.icon)
//    }
//}