package afc.musicapp.di

import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.MergedMetaRead
import afc.musicapp.domain.logic.impure.iface.SongCardDataRetrieve
import afc.musicapp.domain.logic.impure.iface.SongFileImport
import afc.musicapp.domain.logic.impure.iface.SongSearch
import afc.musicapp.domain.logic.impure.iface.SongTagEditor
import afc.musicapp.domain.logic.impure.iface.storage.KvConfig
import afc.musicapp.domain.logic.impure.iface.storage.MetaKeyMappingStorage
import afc.musicapp.domain.logic.impure.iface.storage.ThumbnailStorage
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaFormatConfigRead
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaRead
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardTextRead
import afc.musicapp.domain.logic.impure.iface.storage.read.SongFiles
import afc.musicapp.domain.logic.impure.iface.storage.read.TemplatesConfig
import afc.musicapp.domain.logic.impure.iface.storage.transactional.write.MetadataEdit
import afc.musicapp.domain.logic.impure.iface.storage.write.TemplatesUpdate
import afc.musicapp.domain.logic.impure.iface.storage.write.ThumbnailUpdate
import afc.musicapp.domain.logic.impure.impl.MetaFormatConfigImpl
import afc.musicapp.domain.logic.impure.impl.MusicDirsConfigImpl
import afc.musicapp.domain.logic.impure.impl.SongCardDataRepoImpl
import afc.musicapp.domain.logic.impure.impl.SongFileImportImpl
import afc.musicapp.domain.logic.impure.impl.SongTagEditorImpl
import afc.musicapp.domain.logic.impure.impl.TemplatesConfigImpl
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.impure.impl.storage.KvConfigImpl
import afc.musicapp.domain.logic.impure.impl.storage.MetaKeyMappingStorageImpl
import afc.musicapp.domain.logic.impure.impl.storage.MetaStorageImpl
import afc.musicapp.domain.logic.impure.impl.storage.SongCardTextStorageImpl
import afc.musicapp.domain.logic.impure.impl.storage.SongFilesImpl
import afc.musicapp.domain.logic.impure.impl.storage.SongSearchImpl
import afc.musicapp.domain.logic.impure.impl.storage.ThumbnailStorageImpl
import kotlinx.coroutines.IO
import okio.FileSystem
import okio.SYSTEM
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import org.kodein.di.new

val domainModule = DI.Module("domain") {
    bindSingleton {
        Logger(instance(), instance(), "DEFAULT")
    }

    bindSingleton<Dispatchers> {
        object : Dispatchers {
            override val io = kotlinx.coroutines.Dispatchers.IO
            override val default = kotlinx.coroutines.Dispatchers.Default
            override val main = kotlinx.coroutines.Dispatchers.Main
        }
    }

    bindSingleton { FileSystem.SYSTEM }

    bindSingleton<SongSearch> { new(::SongSearchImpl) }
    bindSingleton<SongFileImport> { new(::SongFileImportImpl) }
    bindSingleton<MetaKeyMappingStorage> { new(::MetaKeyMappingStorageImpl) }
    bindSingleton<SongFiles> { new(::SongFilesImpl) }
    bindSingleton<MetaFormatConfigRead> { new(::MetaFormatConfigImpl) }
    bindSingleton<KvConfig> { new(::KvConfigImpl) }
    bindSingleton<SongCardDataRetrieve> { new(::SongCardDataRepoImpl) }
    bindSingleton<SongTagEditor> { new(::SongTagEditorImpl) }


    bindSingletonOf(::MetaStorageImpl)
    bindSingleton<MetaRead> { instance<MetaStorageImpl>() }
    bindSingleton<MetadataEdit> { instance<MetaStorageImpl>() }
    bindSingleton<MergedMetaRead> { instance<MetaStorageImpl>() }

    bindSingletonOf(::SongCardTextStorageImpl)
    bindSingleton<SongCardTextRead> {  instance<SongCardTextStorageImpl>() }
    bindSingleton<SongCardTextUpdate> { instance<SongCardTextStorageImpl>() }

    bindSingletonOf(::TemplatesConfigImpl)
    bindSingleton<TemplatesConfig> { instance<TemplatesConfigImpl>() }
    bindSingleton<TemplatesUpdate> { instance<TemplatesConfigImpl>() }

    bindSingletonOf(::ThumbnailStorageImpl)
    bindSingleton<ThumbnailUpdate> { instance<ThumbnailStorageImpl>() }
    bindSingleton<ThumbnailStorage> { instance<ThumbnailStorageImpl>() }

    bindSingletonOf(::MusicDirsConfigImpl)
    bindSingleton<MusicDirsConfig> { instance<MusicDirsConfigImpl>() }
}