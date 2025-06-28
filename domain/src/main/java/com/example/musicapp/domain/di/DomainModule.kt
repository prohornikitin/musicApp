package com.example.musicapp.domain.di;

import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.TemplateUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.read.TemplatesConfig
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongAdd
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongRemove
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.TemplatesConfigDbEdit
import com.example.musicapp.domain.logic.impure.impl.SongAddImpl
import com.example.musicapp.domain.logic.impure.impl.SongRemoveImpl
import com.example.musicapp.domain.logic.impure.impl.TemplatesConfigDbEditImpl
import com.example.musicapp.domain.logic.impure.impl.TemplatesConfigImpl
import com.example.musicapp.domain.logic.impure.impl.TemplatesUpdateImpl
import dagger.Binds;
import dagger.Module;

@Module(includes = [DomainModule.BindsModule::class])
class DomainModule {
    @Module
    interface BindsModule {
        @Binds
        fun templateStorageEditor(it: TemplatesUpdateImpl): TemplateUpdate

        @Binds
        fun metaKeyMapping(it: TemplatesConfigDbEditImpl): TemplatesConfigDbEdit

        @Binds
        fun templatesConfig(it: TemplatesConfigImpl): TemplatesConfig

        @Binds
        fun songRemove(it: SongRemoveImpl): SongRemove

        @Binds
        fun songAdd(it: SongAddImpl): SongAdd
    }
}
