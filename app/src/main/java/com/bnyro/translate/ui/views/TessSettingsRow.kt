
package com.bnyro.translate.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bnyro.translate.ext.formatBytes
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TessHelper

@Composable
fun TessSettingsRow(
    packName: String,
    size: Long?,
    selectedLanguage: String,
    onSelect: () -> Unit = {},
    actions: @Composable () -> Unit
) {
    val langName = packName.replace(TessHelper.DATA_FILE_SUFFIX, "")

    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(30.dp)
            )
            .clickable {
                Preferences.put(Preferences.tessLanguageKey, langName)
                onSelect()
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (selectedLanguage == langName) "$packName  ✓"
                else if (size != null) "$packName (${size.formatBytes()})"
                else packName,
                modifier = Modifier
                    .padding(15.dp)
                    .weight(1f)
            )
            actions.invoke()
        }
    }
}