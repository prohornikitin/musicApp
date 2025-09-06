package com.example.musicapp.domain.data

enum class ConfigKey(val raw: String) {
    MAIN_TEMPLATE("main_template"),
    SUB_TEMPLATE("sub_template"),
    META_DELIMITER("meta_delimiter"),
    ;

    companion object {
        fun findByKey(key: String): ConfigKey? {
            return entries.find { it.raw == key }
        }
    }
}