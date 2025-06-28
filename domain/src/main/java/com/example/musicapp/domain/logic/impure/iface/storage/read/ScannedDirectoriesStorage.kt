package com.example.musicapp.domain.logic.impure.iface.storage.read

fun interface ScannedDirectoriesStorage {
    fun getDirectories(): List<String>
}