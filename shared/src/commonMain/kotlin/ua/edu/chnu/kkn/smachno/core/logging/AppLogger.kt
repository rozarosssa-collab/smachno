package ua.edu.chnu.kkn.smachno.core.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

/**
 * Один логер на весь застосунок. Через нього пишемо все, що треба бачити
 * в консолі, щоб не розкидати виклики Logger по коду з різними тегами.
 */
object AppLogger {

    private val logger = Logger.withTag("Smachno")

    fun init(isDebug: Boolean) {
        Logger.setMinSeverity(if (isDebug) Severity.Debug else Severity.Warn)
        logger.i { "Застосунок запущено, режим налагодження: $isDebug" }
    }

    fun d(message: () -> String) = logger.d { message() }

    fun i(message: () -> String) = logger.i { message() }

    fun e(throwable: Throwable? = null, message: () -> String) =
        logger.e(throwable = throwable) { message() }
}
