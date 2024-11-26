
package com.bnyro.translate.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.HistoryItemType
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.screens.HistoryScreen
import com.bnyro.translate.ui.screens.SettingsScreen
import com.bnyro.translate.ui.screens.TranslationPage
import com.bnyro.translate.ui.views.AboutPage

@Composable
fun NavigationHost(
    navController: NavHostController,
    translationModel: TranslationModel
) {
    NavHost(navController = navController, Destination.Translate.route) {
        composable(Destination.Translate.route) {
            TranslationPage(navController, translationModel)
        }
        composable(Destination.History.route) {
            HistoryScreen(navController, translationModel, HistoryItemType.HISTORY, R.string.history, R.string.clear_history)
        }
        composable(Destination.Favorites.route) {
            HistoryScreen(navController, translationModel, HistoryItemType.FAVORITE, R.string.favorites, R.string.clear_favorites)
        }
//        composable(Destination.Settings.route) {
//            SettingsScreen(navController)
//        }
        composable(Destination.About.route) {
            AboutPage(navController)
        }
    }
}
