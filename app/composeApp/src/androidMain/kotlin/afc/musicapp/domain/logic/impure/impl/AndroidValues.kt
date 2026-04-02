package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.logic.impure.iface.PlatformValues
import android.content.Context
import okio.Path
import okio.Path.Companion.toPath

class AndroidValues constructor(
    ctx: Context
) : PlatformValues {
    private val context = ctx.applicationContext

    override val iconsDirectory: Path
        get() = context.filesDir.path.toPath() / "icons"
}