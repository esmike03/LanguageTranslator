

package com.bnyro.translate.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bnyro.translate.R
import java.util.*

object SpeechHelper {
    private lateinit var tts: TextToSpeech
    var ttsAvailable = false

    fun checkPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                0
            )
        }
    }

    fun initTTS(context: Context) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Log.e("TTS", "Initialization Failed")
            } else {
                ttsAvailable = true
            }
        }
    }

    fun speak(context: Context, text: String, language: String) {
        val result = tts.setLanguage(Locale(language))

        if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            Log.e("TTS", "Language is not supported")
            Toast.makeText(context, R.string.language_not_supported, Toast.LENGTH_SHORT).show()
            return
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}
