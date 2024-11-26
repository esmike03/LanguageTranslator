

package com.bnyro.translate.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import java.util.*

class SpeechResultContract: ActivityResultContract<Locale, String?>() {
    override fun createIntent(context: Context, input: Locale): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            .putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            .putExtra(RecognizerIntent.EXTRA_LANGUAGE, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
         if (resultCode != Activity.RESULT_OK) return null

        return intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
            results?.firstOrNull()
        }
    }
}