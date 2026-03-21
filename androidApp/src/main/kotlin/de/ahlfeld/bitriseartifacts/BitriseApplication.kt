package de.ahlfeld.bitriseartifacts

import android.app.Application
import de.ahlfeld.bitriseartifacts.di.initKoin
import org.koin.android.ext.koin.androidContext

class BitriseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BitriseApplication)
        }
    }
}
