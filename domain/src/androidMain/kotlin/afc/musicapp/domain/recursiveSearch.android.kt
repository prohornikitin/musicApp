package afc.musicapp.domain

import okio.FileSystem
import okio.Path

actual fun Path.isAudio(fileSystem: FileSystem): Boolean {
    val mime = this.toFile().toURI().toURL().openConnection().contentType
    return mime.startsWith("audio")
}