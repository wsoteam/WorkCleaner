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
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics
import ru.mail.aslanisl.mobpirate.MobPirate
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private var privatePoliceBtn: Button? = null
    internal var privacyPoliceClicked = false
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private lateinit var mInterstitialAd: InterstitialAd

    val canGoNext = MutableLiveData<Int>()

    var counter: Int = 0
    var max = 0

    init {
        canGoNext.observe(this, Observer {
            counter += it
            if (counter > max) {
                goNext()
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flash_screen)
        Events.logSplash()
        //activateRC()
        handlAd("kek")
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
    }


    private fun handlOnboard(responseString: String?) {
        Log.e("LOL", responseString)
    }

    private fun loadAd() {
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
                Events.logFirstInter()
                mInterstitialAd.show()
            }
        }
    }


    private fun handlAd(interState: String?) {
        /*if (false) {
            loadAd()
        } else {
            Thread {
                TimeUnit.SECONDS.sleep(2)
                canGoNext.postValue(1)
            }.start()
        }*/
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
       /* if (isFirstLaunch()) {
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
        } else {*/
            startActivity(Intent(this, MainActivity::class.java))
            //startActivity(Intent(this, InterRocketAct::class.java))
            finish()
        //}
    }

    private fun isFirstLaunch(): Boolean {
        if (getSharedPreferences(Config.FIRST_LAUNCH, Context.MODE_PRIVATE).getBoolean(Config.FIRST_LAUNCH, true)) {
            getSharedPreferences(Config.FIRST_LAUNCH, Context.MODE_PRIVATE).edit()
                    .putBoolean(Config.FIRST_LAUNCH, false).commit()
            return true
        } else {
            return false
        }
    }

}