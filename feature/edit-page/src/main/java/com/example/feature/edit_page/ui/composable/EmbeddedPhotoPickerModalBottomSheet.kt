package com.example.feature.edit_page.ui.composable

import android.net.Uri
import android.os.Build
import android.widget.photopicker.EmbeddedPhotoPickerFeatureInfo
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.photopicker.compose.EmbeddedPhotoPicker
import androidx.photopicker.compose.ExperimentalPhotoPickerComposeApi
import androidx.photopicker.compose.rememberEmbeddedPhotoPickerState
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 15)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPhotoPickerComposeApi::class)
@Composable
fun EmbeddedPhotoPickerModalBottomSheet(
    sheetState: SheetState,
    onUriPermissionGranted: (List<Uri>) -> Unit,
    onUriPermissionRevoked: (List<Uri>) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val pickerState = rememberEmbeddedPhotoPickerState(
        onSelectionComplete = {
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        onUriPermissionGranted = onUriPermissionGranted,
        onUriPermissionRevoked = onUriPermissionRevoked
    )
    ModalBottomSheet (
        sheetState = sheetState,
        onDismissRequest = {}
    ) {
        Column(Modifier.fillMaxWidth()) {
            EmbeddedPhotoPicker(
                state = pickerState,
                embeddedPhotoPickerFeatureInfo = EmbeddedPhotoPickerFeatureInfo
                    .Builder()
                    .setMaxSelectionLimit(1)
                    .build()
            )
        }
    }
}