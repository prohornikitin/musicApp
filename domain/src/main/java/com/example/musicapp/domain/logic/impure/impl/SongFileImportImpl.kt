package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.FileMetaParser
import com.example.musicapp.domain.logic.impure.iface.FileMetaUpdate
import com.example.musicapp.domain.logic.impure.iface.SongFileImport
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.TemplatesConfigRead
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.ThumbnailStorageWrite
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.MetadataEdit
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.SongCardTextUpdate
import com.example.musicapp.domain.logic.pure.applyTemplate
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import java.io.File
import javax.inject.Inject

class SongFileImportImpl @Inject constructor(
    private val db: DbQueryInterpreter,
    private val metaParser: FileMetaParser,
    private val templatesConfig: TemplatesConfigRead,
    private val metadataEdit: MetadataEdit,
    private val songCardTextUpdate: SongCardTextUpdate,
    private val fileMetaUpdate: FileMetaUpdate,
    private val thumbnailStorageWrite: ThumbnailStorageWrite,
) : SongFileImport {
    override fun importIfFileNotExists(file: File, deleteMetaFromFileAfterImport: Boolean)  {
        val newSongId = db.execute(MainDbSetup.insertSongFileIfNotExists(file.path))?.let(::SongId)
        if (newSongId == null) {
            return
        }
        val meta = metaParser.getFullMetaFromFile(file)
        metadataEdit.insertMeta(newSongId, meta.properties.toList())
        val iconPath = if(meta.icon != null) {
            thumbnailStorageWrite.saveOrJustGetExistingRelativePath(meta.icon)
        } else null
        if (iconPath != null) {
            db.execute(MainDbSetup.updateSongIconPath(newSongId, iconPath))
        }

        val templates = templatesConfig.getTemplates()
        songCardTextUpdate.update(newSongId, SongCardText(
            applyTemplate(templates.main, meta.properties),
            applyTemplate(templates.bottom, meta.properties),
        ))
        if (deleteMetaFromFileAfterImport) {
            fileMetaUpdate.clearMeta(file)
        }
    }

    override fun reloadDataFromFile(file: File) {
        TODO("Not yet implemented")
    }
}