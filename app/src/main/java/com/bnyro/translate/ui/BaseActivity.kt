

package com.bnyro.translate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.util.LocaleHelper
import com.bnyro.translate.util.Preferences

open class BaseActivity: ComponentActivity() {
    lateinit var translationModel: TranslationModel
    var themeMode by mutableStateOf(Preferences.getThemeMode())
    var accentColor by mutableStateOf(Preferences.getAccentColor())

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.updateLanguage(this)

        translationModel = ViewModelProvider(this)[TranslationModel::class.java]

        super.onCreate(savedInstanceState)
    }

    fun showContent(content: @Composable () -> Unit) {
        setContent {
            TranslateYouTheme(themeMode, accentColor?.hexToColor()) {
                content()
            }
        }
    }
}