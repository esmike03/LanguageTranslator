
package com.bnyro.translate.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.obj.TessLanguage
import com.bnyro.translate.util.TessHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TessModel: ViewModel() {
    var availableLanguages by mutableStateOf(emptyList<TessLanguage>())
    var downloadedLanguages by mutableStateOf(emptyList<String>())

    fun init(context: Context) {
        downloadedLanguages = TessHelper.getDownloadedLanguages(context)

        viewModelScope.launch(Dispatchers.IO) {
            availableLanguages = TessHelper.getAvailableLanguages()
        }
    }

    fun refreshDownloadedLanguages(context: Context) {
        downloadedLanguages = TessHelper.getDownloadedLanguages(context)
    }
}