package com.example.musicapp.uistate.config

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.config.SongCardStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongCardStyleEditorVm @Inject constructor() : ViewModel() {
    var config by mutableStateOf(SongCardStyle())

    fun onSave() {
        Log.d("DEBUG", "save")
    }
}