package ua.edu.chnu.kkn.smachno.di

import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.edu.chnu.kkn.smachno.AppViewModel
import ua.edu.chnu.kkn.smachno.data.preferences.AppPreferencesRepository
import ua.edu.chnu.kkn.smachno.domain.PreferencesRepository
import ua.edu.chnu.kkn.smachno.domain.onboarding.IsOnboardingPassedUseCase
import ua.edu.chnu.kkn.smachno.domain.onboarding.MarkOnboardingPassedUseCase
import ua.edu.chnu.kkn.smachno.ui.login.LoginViewModel
import ua.edu.chnu.kkn.smachno.ui.onboarding.OnboardingViewModel

/** Рівень даних: сховище налаштувань. */
private val dataModule = module {
    single<Settings> { Settings() }
    singleOf(::AppPreferencesRepository) bind PreferencesRepository::class
}

/** Галузевий рівень: варіанти використання. */
private val domainModule = module {
    factoryOf(::IsOnboardingPassedUseCase)
    factoryOf(::MarkOnboardingPassedUseCase)
}

/** Презентаційний рівень: моделі екранів. */
private val presentationModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::LoginViewModel)
}

val appModule = module {
    includes(dataModule, domainModule, presentationModule)
}
