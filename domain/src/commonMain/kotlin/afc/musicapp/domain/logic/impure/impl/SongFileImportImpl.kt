package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.FileMetaParser
import afc.musicapp.domain.logic.impure.iface.FileMetaUpdate
import afc.musicapp.domain.logic.impure.iface.SongFileImport
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.read.TemplatesConfig
import afc.musicapp.domain.logic.impure.iface.storage.transactional.write.MetadataEdit
import afc.musicapp.domain.logic.impure.iface.storage.write.ThumbnailUpdate
import afc.musicapp.domain.logic.pure.applyTemplate
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import okio.Path

class SongFileImportImpl(
    private val db: DbQueryInterpreter,
    private val metaParser: FileMetaParser,
    private val templatesConfigRead: TemplatesConfig,
    private val metadataEdit: MetadataEdit,
    private val songCardTextUpdate: SongCardTextUpdate,
    private val fileMetaUpdate: FileMetaUpdate,
    private val thumbnailStorage: ThumbnailUpdate,
) : SongFileImport {
    override suspend fun importIfSongNotExistsAlready(file: Path, deleteMetaFromFileAfterImport: Boolean) {
        val newSongId = db.executeOrDefault(MainDbSetup.insertSongFileIfNotExists(file.toString()))?.let(
            ::SongId
        )
        if (newSongId == null) {
            return
        }
        val meta = metaParser.getFullMetaFromFile(file)
        metadataEdit.insertMeta(newSongId, meta.properties.toList())
        thumbnailStorage.updateIcon(newSongId, meta.icon)
        val templates = templatesConfigRead.getTemplates()
        songCardTextUpdate.update(newSongId, SongCardText(
            applyTemplate(templates.main, meta.properties),
            applyTemplate(templates.bottom, meta.properties),
        )
        )
        if (deleteMetaFromFileAfterImport) {
            fileMetaUpdate.clearMeta(file)
        }
    }

    override suspend fun reloadDataFromFile(file: Path) {
        TODO("Not yet implemented")
    }
}