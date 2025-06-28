package com.example.config

enum class FontStyle(override val value: String) : SerializableEnum {
    Normal("Default"),
    Italic("Italic"),
    ;
}