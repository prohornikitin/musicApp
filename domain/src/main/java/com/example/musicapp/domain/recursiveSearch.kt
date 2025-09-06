package com.example.musicapp.domain

import java.io.File
import java.util.ArrayDeque
import java.util.Queue

fun isAudio(file: File): Boolean {
    val mime = file.toURI().toURL().openConnection().contentType
    return mime.startsWith("audio")
}

fun recursiveSearchFiles(dir: File): List<File> {
    val files = mutableListOf<File>()
    val directories: Queue<File> = ArrayDeque()
    directories.add(dir)
    while(directories.isNotEmpty()) {
        directories.remove().listFiles()?.forEach {
            if (it.isDirectory) {
                directories.add(it)
            } else {
                files.add(it)
            }
        }
    }
    return files
}

fun recursiveSearchMusicFiles(dirs: List<File>): List<File> = dirs
    .flatMap { recursiveSearchFiles(it) }
    .filter(::isAudio)

