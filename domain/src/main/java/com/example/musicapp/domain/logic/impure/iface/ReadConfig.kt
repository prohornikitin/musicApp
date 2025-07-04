package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.ConfigKey

typealias ReadConfig = (ConfigKey) -> String?