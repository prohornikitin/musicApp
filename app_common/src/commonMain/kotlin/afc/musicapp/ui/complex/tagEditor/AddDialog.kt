package afc.musicapp.ui.complex.tagEditor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import afc.musicapp.uistate.vm.TagEditorVm
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.add
import musicapp.app_common.generated.resources.add_tag
import musicapp.app_common.generated.resources.cancel
import musicapp.app_common.generated.resources.tag_name
import org.jetbrains.compose.resources.stringResource


@Composable
fun AddDialog(vm: TagEditorVm) {
    if(vm.dialogShown) {
        AlertDialog(
            onDismissRequest = vm::dismissDialog,
            confirmButton = {
                Button(
                    onClick = vm::addTag,
                    content = { Text(stringResource(Res.string.add)) }
                )
            },
            dismissButton = {
                Button(
                    onClick = vm::dismissDialog,
                    content = { Text(stringResource(Res.string.cancel)) }
                )
            },
            title = {
                Text(stringResource(Res.string.add_tag))
            },
            text = {
                OutlinedTextField(
                    value = vm.dialogAddKey,
                    onValueChange = { vm.dialogAddKey = it },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    placeholder = { Text(stringResource(Res.string.tag_name)) }
                )
            },
        )
    }
}