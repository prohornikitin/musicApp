package afc.musicapp.app_common.di

import afc.musicapp.app_common.uistate.config.SongCardStyleEditorVm
import afc.musicapp.app_common.uistate.config.TemplateEditorVm
import afc.musicapp.app_common.uistate.vm.MainVm
import afc.musicapp.app_common.uistate.vm.PlayerVm
import afc.musicapp.app_common.uistate.vm.TagEditorVm
import afc.musicapp.data.di.dataModule
import afc.musicapp.di.domainModule
import afc.musicapp.domain.logic.impure.iface.logger.LogConfig
import afc.musicapp.domain.logic.pure.logger.LogLevel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf

val appModule = DI.Module("app") {
    import(dataModule)
    import(domainModule)

    bindSingletonOf(::MainVm)
    bindSingletonOf(::PlayerVm)
    bindSingletonOf(::TagEditorVm)
    bindSingletonOf(::TemplateEditorVm)
    bindSingletonOf(::SongCardStyleEditorVm)

    bindSingleton<LogConfig> {
        LogConfig(LogLevel.DEBUG)
    }
}