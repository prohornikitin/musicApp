package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.di.context.ApplicationContext
import afc.musicapp.domain.logic.impure.iface.PlatformValues
import okio.Path
import okio.Path.Companion.toPath

class AndroidValues constructor(
    private val context: ApplicationContext
) : PlatformValues {
    override val iconsDirectory: Path
        get() = context.filesDir.path.toPath() / "icons"
}