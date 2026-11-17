package ua.edu.chnu.kkn.smachno.ui.onboarding

import org.jetbrains.compose.resources.StringResource
import smachno.shared.generated.resources.Res
import smachno.shared.generated.resources.onboarding_favourites_body
import smachno.shared.generated.resources.onboarding_favourites_title
import smachno.shared.generated.resources.onboarding_recipes_body
import smachno.shared.generated.resources.onboarding_recipes_title
import smachno.shared.generated.resources.onboarding_shopping_body
import smachno.shared.generated.resources.onboarding_shopping_title
import smachno.shared.generated.resources.onboarding_steps_body
import smachno.shared.generated.resources.onboarding_steps_title

/**
 * Слайди онбордінгу. Тексти беремо з ресурсів, тому вони перекладаються
 * разом з рештою застосунку.
 */
enum class OnboardingPage(
    val title: StringResource,
    val body: StringResource,
) {
    Recipes(Res.string.onboarding_recipes_title, Res.string.onboarding_recipes_body),
    Steps(Res.string.onboarding_steps_title, Res.string.onboarding_steps_body),
    Favourites(Res.string.onboarding_favourites_title, Res.string.onboarding_favourites_body),
    Shopping(Res.string.onboarding_shopping_title, Res.string.onboarding_shopping_body),
}
