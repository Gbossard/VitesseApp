package com.example.core.ui.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.core.R
import com.example.core.ui.theme.VitesseAppTheme

@Composable
fun PhotoContent(
    modifier: Modifier = Modifier,
    photo: Any?,
    onClick: (() -> Unit)? = null
) {
    val isClickable = onClick != null

    Box(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(3f / 2f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (photo == null) {
            Image(
                painter = painterResource(R.drawable.ic_empty_image_24dp),
                contentDescription = stringResource(R.string.content_description_empty_image),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(32.dp))
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.LightGray)
                    .clickableIf(
                        enabled = isClickable,
                        onClick = onClick
                    ),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        } else {
            AsyncImage(
                model = photo,
                contentDescription = stringResource(R.string.content_description_photo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(32.dp))
                    .clickableIf(
                        enabled = isClickable,
                        onClick = onClick
                    )
            )
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.clickableIf(
    onClick: (() -> Unit)?,
    enabled: Boolean
): Modifier = if (enabled && onClick != null) {
    this.clickable(
        onClick = onClick
    )
} else {
    this
}

@Preview
@Composable
private fun PhotoContentNullPreview() {
    VitesseAppTheme {
        PhotoContent(
            photo = null
        )
    }
}