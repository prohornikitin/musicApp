package afc.musicapp.app_common.ui.complex.tagEditor

import afc.musicapp.app_common.ui.reusable.TagEditItem
import afc.musicapp.app_common.uistate.vm.TagEditorVm
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.itemsIndexed
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.add
import musicapp.app_common.generated.resources.go_back
import musicapp.app_common.generated.resources.redo
import musicapp.app_common.generated.resources.save
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
                    Button(onClick = vm::save, enabled = !vm.isLoading) {
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
                    itemsIndexed(
                        items = vm.tags.toList(),
                    ) { index, it  ->
                        val key = it.first
                        val value = it.second
                        TagEditItem(
                            key,
                            value,
                            onValueChange = { vm.changeTag(index, it) },
                            onDeleteClick = { vm.delete(index) },
                        )
                    }
                }
            }
        }
    }
}