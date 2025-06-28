package com.example.musicapp.data

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.musicapp.R

enum class SongOption(@StringRes val resId: Int) {
    EditMeta(R.string.edit_meta),
    Details(R.string.details),
    Delete(R.string.delete),
    Share(R.string.share),
    ;

    @Composable
    fun title() = stringResource(resId)
}