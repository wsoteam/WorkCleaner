package cleaner.booster.wso.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cleaner.booster.wso.app.common.analytics.Events
import cleaner.booster.wso.app.common.analytics.UserProperties
import cleaner.booster.wso.app.common.tests.ABConfig
import cleaner.booster.wso.app.inapp.premiums.DiamondFrag
import cleaner.booster.wso.app.inapp.RocketAct
import cleaner.booster.wso.app.inapp.premiums.PremiumHostAct
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import ru.mail.aslanisl.mobpirate.MobPirate

class SplashActivity : AppCompatActivity() {
    private var privatePoliceBtn: Button? = null
    internal var privacyPoliceClicked = false
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private lateinit var mInterstitialAd: InterstitialAd

    val canGoNext = MutableLiveData<Int>()

    var counter: Int = 0

    init {
        canGoNext.observe(this, Observer {
            counter += it
            if (counter > 0) {
                goNext()
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flash_screen)
        Events.logSplash()
        activateABTest()
        signInAndInitUser(intent)
        privacyPoliceClicked = false
        privatePoliceBtn = findViewById(R.id.privacyPoliceBtn)
        val htmlTaggedString = "<u>" + getString(R.string.privacy_police) + "</u>"
        val textSpan = android.text.Html.fromHtml(htmlTaggedString)
        privatePoliceBtn!!.text = textSpan
        privatePoliceBtn!!.setOnClickListener {
            privacyPoliceClicked = true
            startActivity(Intent(this@SplashActivity, PrivacyPoliceActivity::class.java))
        }
        //loadAd()
    }

    private fun loadAd() {
        MobileAds.initialize(this, "ca-app-pub-3050564412171997~3819999709")
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = resources.getString(R.string.interstitial)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {

            override fun onAdFailedToLoad(p0: Int) {
                canGoNext.postValue(1)

                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                canGoNext.postValue(1)

                super.onAdClosed()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                mInterstitialAd.show()
            }
        }
    }

    private fun activateABTest() {
        val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setDefaults(R.xml.default_config)

        firebaseRemoteConfig.fetch(3600)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        firebaseRemoteConfig.activateFetched()
                        Events.logSuccess()
                    } else {
                        Events.logError()
                    }
                    setABTestConfig(firebaseRemoteConfig.getString(ABConfig.REQUEST_STRING)
                    )
                }
    }

    private fun setABTestConfig(responseString: String) {
        UserProperties.setABUserProp(responseString)
        getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE).edit()
                .putString(ABConfig.KEY_FOR_SAVE_STATE, ABConfig.DEFAULT)
                .apply()
        canGoNext.postValue(1)
    }

    private fun signInAndInitUser(intent: Intent) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        MobPirate.getInstance()
                .getTargetUrl(this, intent)
        setPirateUser()
    }

    private fun setPirateUser() {
        if (MobPirate.getInstance().clientId != null && MobPirate.getInstance().clientId != "") {
            Log.d("traffic_id: ", MobPirate.getInstance().clientId)
            mFirebaseAnalytics!!.setUserProperty("traffic_id", MobPirate.getInstance().clientId)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, MobPirate.getInstance().clientId)
            bundle.putString(FirebaseAnalytics.Param.MEDIUM, MobPirate.getInstance().clientId)
            bundle.putString(FirebaseAnalytics.Param.SOURCE, MobPirate.getInstance().clientId)
            bundle.putString(FirebaseAnalytics.Param.ACLID, MobPirate.getInstance().clientId)
            bundle.putString(FirebaseAnalytics.Param.CONTENT, MobPirate.getInstance().clientId)
            bundle.putString(FirebaseAnalytics.Param.CP1, MobPirate.getInstance().clientId)
            bundle.putString(FirebaseAnalytics.Param.VALUE, MobPirate.getInstance().clientId)
            mFirebaseAnalytics!!.logEvent("traffic_id", bundle)
            mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
            mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS, bundle)
        }
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        privacyPoliceClicked = false
        super.onResume()
    }

    private fun goNext() {
        if (!privacyPoliceClicked) {
            moveABTest()
        }
    }

    private fun moveABTest() {
        if (isFirstLaunch()) {
            val version =
                    getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, Context.MODE_PRIVATE).getString(ABConfig.KEY_FOR_SAVE_STATE, "")
            var intent = Intent()
            when (version) {
                ABConfig.DEFAULT, ABConfig.A, ABConfig.B, ABConfig.C, ABConfig.D, ABConfig.E ->
                    intent = Intent(this, RocketAct::class.java)
                ABConfig.F, ABConfig.G -> intent =
                        Intent(this, PremiumHostAct::class.java).putExtra(Config.PREM_FROM, Config.PREM_FROM_ONBOARD)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun isFirstLaunch(): Boolean {
        if (getSharedPreferences(Config.FIRST_LAUNCH, Context.MODE_PRIVATE).getBoolean(Config.FIRST_LAUNCH, true)) {
            getSharedPreferences(Config.FIRST_LAUNCH, Context.MODE_PRIVATE).edit()
                    .putBoolean(Config.FIRST_LAUNCH, false)
            return true
        } else {
            return false
        }
    }

}