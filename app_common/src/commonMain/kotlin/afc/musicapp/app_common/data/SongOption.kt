package afc.musicapp.app_common.data

import androidx.compose.runtime.Composable
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class SongOption(val res: StringResource) {
    EditMeta(Res.string.edit_meta),
    Details(Res.string.details),
    DeleteFile(Res.string.delete_file),
    Share(Res.string.share),
    ;

    @Composable
    fun title() = stringResource(res)
}