package afc.musicapp.domain.entities


data class SongCardTemplates(
    val main: Template,
    val bottom: Template,
) {
    fun getUsedKeys(): Set<MetaKey> {
        return main.getUsedKeys() + bottom.getUsedKeys()
    }
}