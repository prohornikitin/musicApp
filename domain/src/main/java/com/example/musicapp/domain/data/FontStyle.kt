package com.example.musicapp.domain.data

enum class FontStyle(override val value: String) : SerializableEnum {
    Normal("Default"),
    Italic("Italic"),
    ;
}