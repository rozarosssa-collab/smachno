package ua.edu.chnu.kkn.smachno.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import org.koin.dsl.module
import java.io.File

actual val platformModule = module {
    single {
        KSafe(
            baseDir = File(System.getProperty("user.home"), ".smachno"),
            config = KSafeConfig(appNamespace = "ua.edu.chnu.kkn.smachno"),
        )
    }
}
