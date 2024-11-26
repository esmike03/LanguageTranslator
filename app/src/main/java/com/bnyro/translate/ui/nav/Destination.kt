

package com.bnyro.translate.ui.nav

sealed class Destination(val route: String) {
    object Translate : Destination("translate")
    object History : Destination("history")
    object Favorites : Destination("favorites")
//    object Settings : Destination("settings")
    object About : Destination("about")
}
