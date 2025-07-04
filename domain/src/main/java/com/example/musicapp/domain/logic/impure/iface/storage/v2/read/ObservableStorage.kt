package com.example.musicapp.domain.logic.impure.iface.storage.v2.read

interface ObservableStorage<D, C> {
    fun get(): D

    fun addChangeCallback(callback: C)
    fun removeChangeCallback(callback: C)
}