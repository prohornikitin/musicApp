package afc.musicapp.ui.complex

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import afc.musicapp.ui.reusable.MenuOpenButton
import afc.musicapp.uistate.config.TemplateEditorVm
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.bottom_template
import musicapp.app_common.generated.resources.main_template
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateEditor(
    vm: TemplateEditorVm,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    CommonSettingsScreen(
        topBar = {
            MenuOpenButton(openDrawer)
        },
        onSave = vm::onSave,
    ) {
        Column(modifier) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.mainTemplate.text,
                onValueChange = vm::onMainTemplateChange,
                visualTransformation = {
                    TransformedText(
                        vm.mainTemplate,
                        OffsetMapping.Companion.Identity
                    )
                },
                label = { Text(stringResource(Res.string.main_template)) }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = vm.subTemplate.text,
                onValueChange = vm::onBottomTemplateChanged,
                visualTransformation = {
                    TransformedText(
                        vm.subTemplate,
                        OffsetMapping.Companion.Identity
                    )
                },
                label = { Text(stringResource(Res.string.bottom_template)) }
            )
        }
    }
}