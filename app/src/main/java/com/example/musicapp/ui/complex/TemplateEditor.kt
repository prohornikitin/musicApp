package com.example.musicapp.ui.complex

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import com.example.musicapp.R
import com.example.musicapp.uistate.config.TemplateEditorVm

@Composable
fun TemplateEditor(
    vm: TemplateEditorVm,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    CommonSettingsScreen(
        openDrawer = openDrawer,
        onSave = vm::onSave,
    ) {
        Column(modifier) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.mainTemplate.text,
                onValueChange = vm::onMainTextChange,
                visualTransformation = {
                    TransformedText(
                        vm.mainTemplate,
                        OffsetMapping.Companion.Identity
                    )
                },
                label = { Text(stringResource(R.string.main_template)) }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.subTemplate.text,
                onValueChange = vm::onBottomTemplateChange,
                visualTransformation = {
                    TransformedText(
                        vm.subTemplate,
                        OffsetMapping.Companion.Identity
                    )
                },
                label = { Text(stringResource(R.string.bottom_template)) }
            )
        }
    }
}