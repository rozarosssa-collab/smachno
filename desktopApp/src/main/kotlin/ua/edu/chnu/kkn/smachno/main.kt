package ua.edu.chnu.kkn.smachno

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.di.initKoin

fun main() {
    AppLogger.init(isDebug = true)
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(1100.dp, 760.dp)),
            title = "Смачно",
        ) {
            App()
        }
    }
}
