package com.example.musicapp.domain.logic.impure.impl.storage.l2

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.KvConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.KvConfigUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.MetaFormatConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l2.write.MetaFormatConfigUpdate
import javax.inject.Inject

class MetaFormatConfigImpl @Inject constructor(
    private val configRead: KvConfigRead,
    private val configUpdate: KvConfigUpdate,
): MetaFormatConfigRead, MetaFormatConfigUpdate {
    override fun getDelimiter(): String {
        return configRead.get(ConfigKey.META_DELIMITER) ?: "/"
    }

    override fun setDelimiter(delimiter: String) {
        configUpdate.update(ConfigKey.META_DELIMITER, delimiter)
    }
}