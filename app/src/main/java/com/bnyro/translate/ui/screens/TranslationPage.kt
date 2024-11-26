

package com.bnyro.translate.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.components.LanguageSelectionComponent
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.nav.Destination
import com.bnyro.translate.ui.views.AdditionalInfoComponent
import com.bnyro.translate.ui.views.TopBar
import com.bnyro.translate.ui.views.TranslationComponent
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.ui.views.SimTranslationComponent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardElevation
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.unit.dp

@SuppressLint("UnrememberedMutableState")
@Composable
fun TranslationPage(
    navHostController: NavController,
    viewModel: TranslationModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refresh(context)
    }

    // Gradient colors for the background
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF836FF2), // Purple
            Color(0xFFD367D4)  // Red Purple
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush) // Apply the gradient background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    mainModel = viewModel,
                    menuItems = listOf(
                        MenuItemData(
                            text = stringResource(id = R.string.history),
                            icon = Icons.Default.History
                        ) {
                            navHostController.navigate(Destination.History.route)
                        },
                        MenuItemData(
                            text = stringResource(id = R.string.favorites),
                            icon = Icons.Default.Favorite
                        ) {
                            navHostController.navigate(Destination.Favorites.route)
                        },
                        MenuItemData(
                            text = stringResource(id = R.string.about),
                            icon = Icons.Default.Info
                        ) {
                            navHostController.navigate(Destination.About.route)
                        }
                    )
                )
            }
        ) { paddingValues ->
            val orientation = LocalConfiguration.current.orientation

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    MainTranslationArea(modifier = Modifier.weight(1f), viewModel = viewModel)

                    LanguageSelectionComponent(viewModel = viewModel)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LanguageSelectionComponent(viewModel = viewModel)

                    MainTranslationArea(modifier = Modifier.weight(2f), viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainTranslationArea(modifier: Modifier, viewModel: TranslationModel) {
    val view = LocalView.current

    var isKeyboardOpen by remember {
        mutableStateOf(false)
    }


    // Detect whether the keyboard is open or closed
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    ElevatedCard(
        modifier = modifier,
        colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Button to toggle the visibility of the translated text


            Spacer(modifier = Modifier.height(10.dp))

            // Conditionally show the translated text based on isTranslationVisible

                TranslationComponent(
                    modifier = Modifier.weight(1f),
                    viewModel = viewModel
                )

            if (Preferences.get(Preferences.showAdditionalInfo, true) && !isKeyboardOpen) {
                AdditionalInfoComponent(
                    translation = viewModel.translation,
                    viewModel = viewModel
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            if (viewModel.simTranslationEnabled) {
                SimTranslationComponent(viewModel)
            } else {
                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .size(70.dp, 2.dp)
                )
            }
        }
    }
}

