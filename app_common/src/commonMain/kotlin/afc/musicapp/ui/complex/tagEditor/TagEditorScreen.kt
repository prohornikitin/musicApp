package afc.musicapp.ui.complex.tagEditor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import afc.musicapp.ui.reusable.TagEditItem
import afc.musicapp.uistate.vm.TagEditorVm
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.add
import musicapp.app_common.generated.resources.add_tag
import musicapp.app_common.generated.resources.cancel
import musicapp.app_common.generated.resources.go_back
import musicapp.app_common.generated.resources.redo
import musicapp.app_common.generated.resources.save
import musicapp.app_common.generated.resources.tag_name
import musicapp.app_common.generated.resources.undo
import org.jetbrains.compose.resources.stringResource

@Composable
fun TagEditorScreen(vm: TagEditorVm, nav: NavHostController) {
    Box(Modifier.imePadding()) {
        AddDialog(vm)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row(modifier = Modifier.padding(4.dp)) {
                    IconButton(
                        onClick = { nav.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.go_back),
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = vm::save) {
                        Text(stringResource(Res.string.save))
                    }
                }
            },
            bottomBar = {
                Row {
                    IconButton(onClick = vm::undo, enabled = vm.canUndo) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = stringResource(Res.string.undo),
                        )
                    }
                    IconButton(onClick = vm::redo, enabled = vm.canRedo) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                            contentDescription = stringResource(Res.string.redo),
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f).height(1.dp))
                    Button(onClick = vm::showAddDialog) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(Res.string.add),
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                LazyColumn(Modifier.weight(1f)) {
                    items(
                        items = vm.tags.toList(),
                        key = { it.first.raw },
                    ) {
                        val key = it.first
                        val value = it.second
                        TagEditItem(
                            key,
                            value,
                            onValueChange = { vm.changeTag(key, it) },
                            onDeleteClick = { vm.delete(key) },
                        )
                    }
                }
            }
        }
    }
}