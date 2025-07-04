package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.musicapp.domain.data.MetaKey

typealias GetMetaKeyMappings = () -> Map<String, MetaKey>