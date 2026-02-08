package afc.musicapp.di

import afc.musicapp.domain.logic.impure.iface.logger.LogConfig
import afc.musicapp.domain.logic.pure.logger.LogLevel
import afc.musicapp.uistate.config.SongCardStyleEditorVm
import afc.musicapp.uistate.config.TemplateEditorVm
import afc.musicapp.uistate.vm.MainVm
import afc.musicapp.uistate.vm.PlayerVm
import afc.musicapp.uistate.vm.TagEditorVm
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf

val appModule = DI.Module("app") {
    import(domainModule)

    bindSingletonOf(::MainVm)
    bindSingletonOf(::PlayerVm)
    bindSingletonOf(::TagEditorVm)
    bindSingletonOf(::TemplateEditorVm)
    bindSingletonOf(::SongCardStyleEditorVm)

    bindSingleton<LogConfig> {
        object : LogConfig {
            override val currentLevel: LogLevel
                get() = LogLevel.DEBUG
        }
    }
}