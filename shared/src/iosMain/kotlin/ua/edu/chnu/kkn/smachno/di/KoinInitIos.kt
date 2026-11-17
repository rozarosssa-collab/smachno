package ua.edu.chnu.kkn.smachno.di

import ua.edu.chnu.kkn.smachno.core.logging.AppLogger

/** Викликається зі Swift при старті iOS-застосунку. */
fun initKoinIos() {
    AppLogger.init(isDebug = true)
    initKoin()
}
