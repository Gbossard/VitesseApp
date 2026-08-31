package com.example.feature.edit_page.ui.composable

import android.os.Build
import android.os.ext.SdkExtensions

fun isEmbeddedPhotoPickerSupported(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
        SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 15
}