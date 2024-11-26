
package com.bnyro.translate.ui.views

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bnyro.translate.R
import com.bnyro.translate.ui.components.ButtonWithIcon
import com.bnyro.translate.ui.components.TranslationField
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.util.Preferences
import kotlinx.coroutines.launch

@Composable
fun TranslationComponent(
    modifier: Modifier,
    viewModel: TranslationModel,
    showLanguageSelector: Boolean = true
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    var hasClip by remember {
        mutableStateOf(false)
    }

    // State for toggling visibility of the translated text
    var hideOutput by remember { mutableStateOf(true) }

    LaunchedEffect(Unit, clipboard) {
        hasClip = clipboard.hasText() && !clipboard.getText()?.toString().isNullOrBlank()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            TranslationField(
                translationModel = viewModel,
                isSourceField = true,
                text = viewModel.insertedText,
                language = viewModel.sourceLanguage,
                showLanguageSelector = showLanguageSelector,
                setLanguage = {
                    if (it == viewModel.targetLanguage) {
                        viewModel.targetLanguage = viewModel.sourceLanguage
                    }
                    viewModel.sourceLanguage = it
                }
            ) {
                viewModel.insertedText = it
                hasClip = clipboard.hasText()
                viewModel.enqueueTranslation(context)
            }

            if (viewModel.translating) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                )
            } else {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .size(70.dp, 1.dp)
                )
            }

            if (hasClip && viewModel.insertedText.isBlank()) {
                Row {
                    ButtonWithIcon(
                        text = stringResource(R.string.paste),
                        icon = Icons.Default.ContentPaste
                    ) {
                        viewModel.insertedText = clipboard.getText()?.toString().orEmpty()
                        viewModel.enqueueTranslation(context)
                    }

                    Spacer(modifier = Modifier.width(0.dp))

                    ButtonWithIcon(
                        text = stringResource(R.string.forget),
                        icon = Icons.Default.Clear
                    ) {
                        hasClip = false

                        val manager =
                            ContextCompat.getSystemService(context, ClipboardManager::class.java)
                                ?: return@ButtonWithIcon

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            manager.clearPrimaryClip()
                        } else {
                            manager.setPrimaryClip(ClipData(null))
                        }

                        viewModel.clearTranslation()
                    }
                }
            } else if (
                viewModel.insertedText.isNotBlank() &&
                !Preferences.get(Preferences.translateAutomatically, true)
            ) {
                ButtonWithIcon(
                    text = stringResource(R.string.translate),
                    icon = Icons.Default.Translate
                ) {
                    viewModel.translateNow(context)
                }
            }

            // Use Box with modifier explicitly applied to the button for top-right alignment

                ButtonWithIcon(
                    text = if (hideOutput) stringResource(R.string.show_translation) else stringResource(R.string.hide_translation),
                    icon = Icons.Default.Translate
                ) {
                    hideOutput = !hideOutput
                }

            // Conditionally show the translated text based on hideOutput
            if (!hideOutput) {
                TranslationField(
                    translationModel = viewModel,
                    isSourceField = false,
                    text = viewModel.translation.translatedText,
                    language = viewModel.targetLanguage,
                    showLanguageSelector = showLanguageSelector,
                    setLanguage = {
                        if (it == viewModel.sourceLanguage) {
                            viewModel.sourceLanguage = viewModel.targetLanguage
                        }
                        viewModel.targetLanguage = it
                    }
                )
            }
        }

        if (scrollState.value > 100) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0)
                    }
                }
            ) {
                Icon(Icons.Default.ArrowUpward, null)
            }
        }
    }
}