package com.example.musicapp.ui.complex

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.musicapp.R

@Composable
fun CommonSettingsScreen(
    onSave: () -> Unit,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = topBar,
        bottomBar = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSave,
            ) { Text(stringResource(R.string.save)) }
        },
        modifier = Modifier.Companion.fillMaxSize().padding(8.dp),
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}