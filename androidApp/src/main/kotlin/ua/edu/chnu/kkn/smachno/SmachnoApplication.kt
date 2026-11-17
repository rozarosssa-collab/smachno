package ua.edu.chnu.kkn.smachno

import android.app.Application
import org.koin.android.ext.koin.androidContext
import ua.edu.chnu.kkn.smachno.core.logging.AppLogger
import ua.edu.chnu.kkn.smachno.di.initKoin

class SmachnoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLogger.init(isDebug = BuildConfig.DEBUG)
        initKoin {
            androidContext(this@SmachnoApplication)
        }
    }
}
