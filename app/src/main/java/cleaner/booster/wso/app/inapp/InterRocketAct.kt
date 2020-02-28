package cleaner.booster.wso.app.inapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import cleaner.booster.wso.app.MainActivity
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.SubscriptionProvider
import cleaner.booster.wso.app.common.analytics.Events
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.rocket_act.*

class InterRocketAct : AppCompatActivity(R.layout.rocket_act) {
    var mInterstitialAd: InterstitialAd
    val canGoNext = MutableLiveData<Int>()
    var counter: Int = 0
    var max = 1

    init {
        mInterstitialAd = InterstitialAd(this)

        canGoNext.observe(this, Observer {
            counter += it
            if (counter > max) {
                if (mInterstitialAd.isLoaded){
                    mInterstitialAd.show()
                }else{
                    openNextScreen()
                }
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Events.logRocket()
        tvAnimText.visibility = View.INVISIBLE
        handlAd()
    }

    private fun handlAd() {
        if (!SubscriptionProvider.hasSubscription()) {
            mInterstitialAd.adUnitId = resources.getString(R.string.interstitial)
            mInterstitialAd.loadAd(AdRequest.Builder().build())
            mInterstitialAd.adListener = object : AdListener() {

                override fun onAdFailedToLoad(p0: Int) {
                    super.onAdFailedToLoad(p0)
                    canGoNext.postValue(1)
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    openNextScreen()
                }

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    Events.logFirstInter()
                    canGoNext.postValue(1)
                }
            }
        }else{
            canGoNext.postValue(1)
        }
    }


    private fun animText() {
        var counter = 1
        object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                canGoNext.postValue(1)
            }

            override fun onTick(millisUntilFinished: Long) {
                if (counter == 2 || counter == 5 || counter == 8 || counter == 11) {
                    tvAnimText.text = resources.getText(R.string.ab_optimization)
                            .toString()
                            .plus(".")
                } else if (counter == 3 || counter == 6 || counter == 9 || counter == 12) {
                    tvAnimText.text = resources.getText(R.string.ab_optimization)
                            .toString()
                            .plus("..")
                } else if (counter == 4 || counter == 7 || counter == 10) {
                    tvAnimText.text = resources.getText(R.string.ab_optimization)
                }
                counter++
            }
        }.start()
    }

    private fun openNextScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        var avdRocket = AnimatedVectorDrawableCompat.create(this, R.drawable.rocket_anim)

        animText()
        ivAnimRocket.setImageDrawable(avdRocket)
        avdRocket?.start()
    }

    override fun onBackPressed() {

    }
}