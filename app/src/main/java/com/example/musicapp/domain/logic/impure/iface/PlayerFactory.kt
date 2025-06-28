package com.example.musicapp.domain.logic.impure.iface

import androidx.media3.common.Player

interface PlayerFactory {
    suspend fun get(): Player
}