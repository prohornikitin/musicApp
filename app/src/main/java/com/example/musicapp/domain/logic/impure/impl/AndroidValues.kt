package com.example.musicapp.domain.logic.impure.impl

import android.content.Context
import com.example.musicapp.domain.logic.impure.iface.PlatformValues
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidValues @Inject constructor(
    @ApplicationContext private val context: Context
) : PlatformValues {
    override val iconsDir: String
        get() = context.filesDir.path + "/icons"
}