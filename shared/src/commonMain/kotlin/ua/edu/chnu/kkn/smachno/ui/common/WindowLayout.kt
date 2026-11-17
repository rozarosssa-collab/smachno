package ua.edu.chnu.kkn.smachno.ui.common

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

/**
 * true - вікно широке (планшет, десктоп, телефон у ландшафті), тоді екран
 * малюється у дві колонки. false - вузьке, усе йде однією колонкою.
 * Поріг - 600 dp, це межа WIDTH_DP_MEDIUM у Material 3.
 */
@Composable
fun isWideWindow(): Boolean =
    currentWindowAdaptiveInfoV2().windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
