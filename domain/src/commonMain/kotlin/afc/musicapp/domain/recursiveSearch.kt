package afc.musicapp.domain

import okio.FileSystem
import okio.Path


expect fun Path.isAudio(fileSystem: FileSystem): Boolean

fun recursiveSearchMusicFiles(dir: Path, fileSystem: FileSystem): List<Path> {
    return fileSystem.listRecursively(dir)
        .filter { fileSystem.metadataOrNull(it)?.isRegularFile == true && it.isAudio(fileSystem)}
        .toList()
}


