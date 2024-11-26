

package com.bnyro.translate.ui

import android.os.Bundle
import androidx.navigation.compose.rememberNavController
import com.bnyro.translate.ui.nav.NavigationHost

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showContent {
            val navController = rememberNavController()
            NavigationHost(navController, translationModel)
        }
    }

    override fun onStop() {
        translationModel.saveSelectedLanguages()
        super.onStop()
    }
}
