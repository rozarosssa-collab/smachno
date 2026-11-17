package ua.edu.chnu.kkn.smachno.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

/** Спільна точка запуску Koin. Кожна платформа додає свою частину через config. */
fun initKoin(config: KoinAppDeclaration? = null): KoinApplication = startKoin {
    includes(config)
    modules(appModule)
}
