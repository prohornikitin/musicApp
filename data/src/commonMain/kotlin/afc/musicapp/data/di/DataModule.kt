package afc.musicapp.data.di

import afc.musicapp.data.ConfigImpl
import afc.musicapp.data.LowLevelConfigImpl
import afc.musicapp.data.MetaKeyMappingStorageImpl
import afc.musicapp.data.SongFilesImpl
import afc.musicapp.data.SongSearchImpl
import afc.musicapp.data.StorageImpl
import afc.musicapp.data.ThumbnailStorageImpl
import afc.musicapp.data.impure.iface.ConfigCache
import afc.musicapp.data.impure.iface.LowLevelConfig
import afc.musicapp.data.impure.impl.ConfigCacheImpl
import afc.musicapp.domain.logic.impure.iface.SongSearch
import afc.musicapp.domain.logic.impure.iface.SongTagEditor
import afc.musicapp.domain.logic.impure.iface.storage.MusicDirConfig
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaFormatConfigRead
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMappingRead
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaRead
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardDataRead
import afc.musicapp.domain.logic.impure.iface.storage.read.SongFiles
import afc.musicapp.domain.logic.impure.iface.storage.write.MetadataEdit
import afc.musicapp.domain.logic.impure.impl.SongCardDataRepoImpl
import afc.musicapp.domain.logic.impure.impl.SongTagEditorImpl
import afc.musicapp.domain.logic.impure.iface.storage.TemplatesConfig
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardTextRead
import afc.musicapp.domain.logic.impure.iface.storage.ThumbnailStorage
import afc.musicapp.domain.logic.impure.iface.SongFileImport
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import org.kodein.di.new

val dataModule = DI.Module("data") {
    import(platformModule)

    bindSingleton<LowLevelConfig> { new(::LowLevelConfigImpl) }
    bindSingleton<ConfigCache> { new(::ConfigCacheImpl) }

    bindSingleton<MusicDirConfig> { new(::ConfigImpl) }
    bindSingleton<MetaFormatConfigRead> { new(::ConfigImpl) }

    bindSingleton<SongSearch> { new(::SongSearchImpl) }
    bindSingleton<MetaKeyMappingRead> { new(::MetaKeyMappingStorageImpl) }
    bindSingleton<SongFiles> { new(::SongFilesImpl) }
    bindSingleton<SongCardDataRead> { new(::SongCardDataRepoImpl) }
    bindSingleton<SongTagEditor> { new(::SongTagEditorImpl) }


    bindSingletonOf(::StorageImpl)
    bindSingleton<MetaRead> { instance<StorageImpl>() }
    bindSingleton<MetadataEdit> { instance<StorageImpl>() }
    bindSingleton<SongFileImport> { instance<StorageImpl>() }
    bindSingleton<SongCardTextRead> {  instance<StorageImpl>() }

    bindSingleton<TemplatesConfig> { instance<StorageImpl>() }

    bindSingletonOf(::ThumbnailStorageImpl)
    bindSingleton<ThumbnailStorage> { instance<ThumbnailStorageImpl>() }
}