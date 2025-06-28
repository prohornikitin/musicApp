package com.example.config

enum class FontFamily(override val value: String) : SerializableEnum {
    Default("Default"),
    SansSerif("SansSerif"),
    Serif("Serif"),
    Monospace("Monospace"),
    Cursive("Cursive"),
    ;
}