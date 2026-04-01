package com.thorn11166.unchainedtorbox.utilities

import android.app.Activity
import android.app.Application
import com.thorn11166.unchainedtorbox.BuildConfig
import ly.count.android.sdk.Countly
import ly.count.android.sdk.CountlyConfig
import timber.log.Timber

object TelemetryManager {
    fun onStart(activity: Activity) {
        Countly.sharedInstance().onStart(activity)
    }

    fun onStop() {
        Countly.sharedInstance().onStop()
    }

    fun onCreate(application: Application) {
        // remove these lines from the release file
        Timber.plant(Timber.DebugTree())

        val config: CountlyConfig =
            CountlyConfig(application, BuildConfig.COUNTLY_APP_KEY, BuildConfig.COUNTLY_URL)
                // if true will print internal countly logs to the console
                .setLoggingEnabled(false)
        // .setParameterTamperingProtectionSalt("SampleSalt")
        config.crashes.enableCrashReporting()

        Countly.sharedInstance().init(config)
    }
}
