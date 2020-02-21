package cleaner.booster.wso.app

import android.app.Activity
import android.content.Context
import com.amplitude.api.Amplitude
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.rt.ModuleApplication
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import ru.mail.aslanisl.mobpirate.MobPirate

class MyApp : ModuleApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }


    override fun onCreate() {
        super.onCreate()
        sInstance = this
        SubscriptionProvider.init(this)
        FacebookSdk.sdkInitialize(applicationContext)
        setAutoLogAppEventsEnabled(true)
        AppEventsLogger.activateApp(this)
        MobPirate.getInstance().init(this, getString(R.string.facebook_app_id))
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this, "ca-app-pub-3050564412171997~3819999709") //ca-app-pub-9387354664905418~6073119457
        // Создание расширенной конфигурации библиотеки.
        val config = YandexMetricaConfig.newConfigBuilder("e21fc696-1f3b-47e8-990a-9d482c4df2f1").build()
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(applicationContext, config)
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this)
        Amplitude.getInstance()
            .trackSessionEvents(true)
        Amplitude.getInstance()
            .initialize(this, "fbf1ac4b1567a2ae7c49981929f42eae")
            .enableForegroundTracking(this)
    }

    companion object {

        private lateinit var sInstance: MyApp

        fun getInstance(): MyApp {
            return sInstance
        }

        /**
         * Returns an instance of [MyApp] attached to the passed activity.
         */
        fun getInstance(activity: Activity): MyApp {
            return activity.application as MyApp
        }
    }

}