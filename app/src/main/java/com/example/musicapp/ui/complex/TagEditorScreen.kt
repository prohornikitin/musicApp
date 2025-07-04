package com.example.musicapp.ui.complex

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicapp.ui.reusable.TagEditItem
import com.example.musicapp.uistate.TagEditorVm

@Composable
fun TagEditorScreen(vm: TagEditorVm) {
    LazyColumn(Modifier) {
        items(
            items = vm.tags.toList(),
            key = { it.first },
        ) {
            val key = it.first
            val value = it.second
            TagEditItem(
                key,
                value,
                onValueChange = { vm.changeTag(key, it) },
                onDeleteClick = { vm.delete(key, value) },
            )
        }
    }
}