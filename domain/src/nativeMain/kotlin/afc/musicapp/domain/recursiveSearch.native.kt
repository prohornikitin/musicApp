package com.example.musicapp.domain

import okio.FileSystem
import okio.Path

actual fun Path.isAudio(fileSystem: FileSystem): Boolean {
    val mime = this..toURI().toURL().openConnection().contentType
    return mime.startsWith("audio")
}