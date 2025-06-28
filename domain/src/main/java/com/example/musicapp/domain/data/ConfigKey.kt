package com.example.musicapp.domain.data

enum class ConfigKey(val key: String) {
    MAIN_TEMPLATE("main_template"),
    SUB_TEMPLATE("sub_template"),
    SCAN_DIRECTORIES("scan_directories"),
    ;

    companion object {
        fun findByKey(key: String): ConfigKey? {
            return entries.find { it.key == key }
        }
    }
}