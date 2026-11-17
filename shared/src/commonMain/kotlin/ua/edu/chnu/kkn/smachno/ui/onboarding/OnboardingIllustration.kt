package ua.edu.chnu.kkn.smachno.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Картинки до слайдів намальовані просто на Canvas, щоб не тягнути
 * у проєкт растрові файли.
 */
@Composable
fun OnboardingIllustration(page: OnboardingPage, modifier: Modifier = Modifier) {
    val main = MaterialTheme.colorScheme.primary
    val soft = MaterialTheme.colorScheme.tertiary

    Canvas(modifier) {
        val unit = size.minDimension
        when (page) {
            OnboardingPage.Recipes -> drawBowl(unit, main, soft)
            OnboardingPage.Steps -> drawSteps(unit, main, soft)
            OnboardingPage.Favourites -> drawHeart(unit, main, soft)
            OnboardingPage.Shopping -> drawBag(unit, main, soft)
        }
    }
}

/** Миска з трьома кружечками - страва. */
private fun DrawScope.drawBowl(unit: Float, main: Color, soft: Color) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val bowlWidth = unit * 0.76f
    val bowlHeight = unit * 0.40f

    repeat(3) { index ->
        drawCircle(
            color = soft,
            radius = unit * 0.07f,
            center = Offset(cx + (index - 1) * unit * 0.19f, cy - unit * 0.17f),
        )
    }
    drawArc(
        color = main,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(cx - bowlWidth / 2f, cy - bowlHeight / 2f),
        size = Size(bowlWidth, bowlHeight * 2f),
    )
    drawRoundRect(
        color = soft,
        topLeft = Offset(cx - bowlWidth * 0.60f, cy + bowlHeight * 0.92f),
        size = Size(bowlWidth * 1.20f, unit * 0.06f),
        cornerRadius = CornerRadius(unit * 0.03f),
    )
}

/** Три рядки списку - кроки рецепта. */
private fun DrawScope.drawSteps(unit: Float, main: Color, soft: Color) {
    val left = size.width / 2f - unit * 0.34f
    val top = size.height / 2f - unit * 0.26f
    val rowHeight = unit * 0.10f
    val gap = unit * 0.16f

    repeat(3) { index ->
        val y = top + index * gap
        drawCircle(
            color = if (index == 0) main else soft,
            radius = rowHeight / 2f,
            center = Offset(left, y + rowHeight / 2f),
        )
        drawRoundRect(
            color = if (index == 0) main else soft,
            topLeft = Offset(left + rowHeight, y + rowHeight * 0.25f),
            size = Size(unit * (0.56f - index * 0.09f), rowHeight * 0.5f),
            cornerRadius = CornerRadius(rowHeight * 0.25f),
        )
    }
}

/** Серце - обране. */
private fun DrawScope.drawHeart(unit: Float, main: Color, soft: Color) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val half = unit * 0.30f

    val heart = Path().apply {
        moveTo(cx, cy + half)
        cubicTo(cx - half * 2.0f, cy - half * 0.30f, cx - half * 0.70f, cy - half * 1.40f, cx, cy - half * 0.35f)
        cubicTo(cx + half * 0.70f, cy - half * 1.40f, cx + half * 2.0f, cy - half * 0.30f, cx, cy + half)
        close()
    }
    drawPath(heart, color = main)
    drawCircle(color = soft, radius = unit * 0.05f, center = Offset(cx + half * 1.15f, cy - half * 0.95f))
}

/** Пакет з ручкою - список покупок. */
private fun DrawScope.drawBag(unit: Float, main: Color, soft: Color) {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val bagWidth = unit * 0.56f
    val bagHeight = unit * 0.50f
    val top = cy - bagHeight * 0.25f

    drawArc(
        color = soft,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - bagWidth * 0.28f, top - bagWidth * 0.28f),
        size = Size(bagWidth * 0.56f, bagWidth * 0.56f),
        style = Stroke(width = unit * 0.045f),
    )
    drawRoundRect(
        color = main,
        topLeft = Offset(cx - bagWidth / 2f, top),
        size = Size(bagWidth, bagHeight),
        cornerRadius = CornerRadius(unit * 0.06f),
    )
    drawRoundRect(
        color = soft,
        topLeft = Offset(cx - bagWidth * 0.22f, top + bagHeight * 0.42f),
        size = Size(bagWidth * 0.44f, unit * 0.045f),
        cornerRadius = CornerRadius(unit * 0.02f),
    )
}
