package ua.edu.chnu.kkn.smachno.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

private val default = Typography()

/** Заголовки - Unbounded, звичайний текст - Inter. Обидва шрифти мають кирилицю. */
fun appTypography(displayFont: Font, bodyFont: Font): Typography {
    val display = FontFamily(displayFont)
    val body = FontFamily(bodyFont)

    return Typography(
        displayLarge = default.displayLarge.copy(fontFamily = display),
        displayMedium = default.displayMedium.copy(fontFamily = display),
        displaySmall = default.displaySmall.copy(fontFamily = display),
        headlineLarge = default.headlineLarge.copy(fontFamily = display),
        headlineMedium = default.headlineMedium.copy(fontFamily = display),
        headlineSmall = default.headlineSmall.copy(fontFamily = display),
        titleLarge = default.titleLarge.copy(fontFamily = display),
        titleMedium = default.titleMedium.copy(fontFamily = body),
        titleSmall = default.titleSmall.copy(fontFamily = body),
        bodyLarge = default.bodyLarge.copy(fontFamily = body),
        bodyMedium = default.bodyMedium.copy(fontFamily = body),
        bodySmall = default.bodySmall.copy(fontFamily = body),
        labelLarge = default.labelLarge.copy(fontFamily = body),
        labelMedium = default.labelMedium.copy(fontFamily = body),
        labelSmall = default.labelSmall.copy(fontFamily = body),
    )
}
