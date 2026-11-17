package ua.edu.chnu.kkn.smachno.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.onboarding_counter
import smachno.shared.generated.resources.onboarding_next
import smachno.shared.generated.resources.onboarding_skip
import smachno.shared.generated.resources.onboarding_start
import ua.edu.chnu.kkn.smachno.ui.common.CenteredScrollColumn
import ua.edu.chnu.kkn.smachno.ui.common.ObserveEvents
import ua.edu.chnu.kkn.smachno.ui.common.isWideWindow

private val DotSize = 8.dp
private val ActiveDotWidth = 24.dp
private val IllustrationNarrow = 200.dp
private val IllustrationWide = 280.dp
private val TextMaxWidth = 440.dp
private const val OffscreenScale = 0.8f

@Composable
internal fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    ObserveEvents(viewModel.events) { event ->
        when (event) {
            OnboardingEvent.OnboardingPassed -> onFinished()
        }
    }

    val pages = remember { OnboardingPage.entries }
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    val complete: () -> Unit = { viewModel.onAction(OnboardingAction.Complete) }
    val next: () -> Unit = next@{
        if (pagerState.currentPage == pages.lastIndex) {
            complete()
            return@next
        }
        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
    }
    val selectPage: (Int) -> Unit = { page ->
        scope.launch { pagerState.animateScrollToPage(page) }
    }

    // Стан один, розмітки дві: вибір залежить від ширини вікна.
    if (isWideWindow()) {
        OnboardingWide(pages, pagerState, next, complete, selectPage, modifier)
    } else {
        OnboardingNarrow(pages, pagerState, next, complete, selectPage, modifier)
    }
}

@Composable
private fun OnboardingNarrow(
    pages: List<OnboardingPage>,
    pagerState: PagerState,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onSelectPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastPage = pagerState.currentPage == pages.lastIndex

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PageCounter(pagerState.currentPage, pages.size)
            TextButton(onClick = onSkip) { Text(stringResource(Res.string.onboarding_skip)) }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IllustrationPlate(
                    page = pages[index],
                    illustrationSize = IllustrationNarrow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(260.dp)
                        .graphicsLayer { applyPageOffset(pagerState.pageOffset(index)) },
                )
                PageText(pages[index], centered = true)
            }
        }

        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 12.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PageIndicator(pages.size, pagerState.currentPage, onSelectPage)
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(14.dp),
            ) {
                Text(
                    stringResource(
                        if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next
                    )
                )
            }
        }
    }
}

@Composable
private fun OnboardingWide(
    pages: List<OnboardingPage>,
    pagerState: PagerState,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onSelectPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastPage = pagerState.currentPage == pages.lastIndex

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding(),
    ) {
        // Ліва колонка - сама картинка, гортається тим самим пейджером.
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        ) { index ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                OnboardingIllustration(
                    page = pages[index],
                    modifier = Modifier
                        .size(IllustrationWide)
                        .graphicsLayer { applyPageOffset(pagerState.pageOffset(index)) },
                )
            }
        }

        // Права колонка - текст поточного слайда й керування.
        CenteredScrollColumn(
            spacing = 28.dp,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentPadding = PaddingValues(40.dp),
        ) {
            AnimatedContent(
                targetState = pages[pagerState.currentPage],
                transitionSpec = { fadeIn() togetherWith fadeOut() },
            ) { page ->
                Column(
                    modifier = Modifier.widthIn(max = TextMaxWidth),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PageCounter(page.ordinal, pages.size)
                    PageText(page, centered = false)
                }
            }
            PageIndicator(pages.size, pagerState.currentPage, onSelectPage)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(onClick = onNext, contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp)) {
                    Text(
                        stringResource(
                            if (isLastPage) Res.string.onboarding_start else Res.string.onboarding_next
                        )
                    )
                }
                TextButton(onClick = onSkip) { Text(stringResource(Res.string.onboarding_skip)) }
            }
        }
    }
}

@Composable
private fun PageText(page: OnboardingPage, centered: Boolean) {
    val alignment = if (centered) TextAlign.Center else TextAlign.Start
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        Text(
            text = stringResource(page.title),
            style = if (centered) {
                MaterialTheme.typography.headlineSmall
            } else {
                MaterialTheme.typography.displaySmall
            },
            textAlign = alignment,
        )
        Text(
            text = stringResource(page.body),
            style = if (centered) {
                MaterialTheme.typography.bodyMedium
            } else {
                MaterialTheme.typography.bodyLarge
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = alignment,
        )
    }
}

@Composable
private fun IllustrationPlate(
    page: OnboardingPage,
    illustrationSize: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center,
    ) {
        OnboardingIllustration(page, Modifier.size(illustrationSize))
    }
}

/**
 * Наскільки сторінка відійшла від центру: 0 - рівно по центру, 1 - повністю збоку.
 * Рахується під час свайпу, тому значення дробове.
 */
private fun PagerState.pageOffset(page: Int): Float =
    ((currentPage - page) + currentPageOffsetFraction).absoluteValue.coerceAtMost(1f)

/** Картинка, що відходить убік, бліднішає і трохи зменшується. */
private fun GraphicsLayerScope.applyPageOffset(offset: Float) {
    alpha = 1f - offset
    val scale = 1f - (1f - OffscreenScale) * offset
    scaleX = scale
    scaleY = scale
}

@Composable
private fun PageCounter(currentPage: Int, pageCount: Int) {
    Text(
        text = stringResource(
            Res.string.onboarding_counter,
            (currentPage + 1).toString().padStart(2, '0'),
            pageCount.toString().padStart(2, '0'),
        ),
        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val isCurrent = index == currentPage
            val width by animateDpAsState(if (isCurrent) ActiveDotWidth else DotSize)
            Box(
                modifier = Modifier
                    .size(width = width, height = DotSize)
                    .clip(CircleShape)
                    .background(
                        if (isCurrent) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                    .clickable { onSelect(index) },
            )
        }
    }
}
