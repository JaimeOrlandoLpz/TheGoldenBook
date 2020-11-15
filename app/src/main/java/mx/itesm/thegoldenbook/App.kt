package mx.itesm.thegoldenbook

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import mx.itesm.thegoldenbook.managers.PreferenceManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)

        PreferenceManager.instance.init(this)
    }
}