package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.data.pure.sql.DbSetupSql
import afc.musicapp.data.pure.sql.Table

object MainDbSetup : DbSetupSql {
    override val currentVersion: Int = 1

    override val tables: List<Table> = listOf(
        Tables.Song,
        Tables.Meta,
        Tables.GenTemplate,
        Tables.KvConfig,
        Tables.MetaKeyMappings,
    )
}