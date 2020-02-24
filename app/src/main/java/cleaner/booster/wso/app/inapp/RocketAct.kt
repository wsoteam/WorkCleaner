package cleaner.booster.wso.app.inapp

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.common.analytics.Events
import cleaner.booster.wso.app.common.tests.ABConfig
import cleaner.booster.wso.app.inapp.graphs.BatteryGraphAct
import cleaner.booster.wso.app.inapp.graphs.SecurityGraphAct
import cleaner.booster.wso.app.inapp.premiums.PremiumHostAct
import kotlinx.android.synthetic.main.rocket_act.ivAnimRocket
import kotlinx.android.synthetic.main.rocket_act.tvAnimText

class RocketAct : AppCompatActivity(R.layout.rocket_act) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Events.logRocket()
    }

    private fun animText() {
        var counter = 1
        object : CountDownTimer(12000, 1000) {
            override fun onFinish() {
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
        val abVersion = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, Context.MODE_PRIVATE).getString(ABConfig.KEY_FOR_SAVE_STATE, "")
        when(abVersion){
            ABConfig.DEFAULT, ABConfig.B -> openBatteryScreen()
            ABConfig.A, ABConfig.C -> openSecurityScreen()
            ABConfig.D, ABConfig.E -> openPremScreen()
        }
    }

    private fun openBatteryScreen(){
        startActivity(Intent(this, BatteryGraphAct::class.java))
        finish()
    }

    private fun openPremScreen(){
        startActivity(Intent(this, PremiumHostAct::class.java))
        finish()
    }

    private fun openSecurityScreen(){
        startActivity(Intent(this, SecurityGraphAct::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        var avdRocket = AnimatedVectorDrawableCompat.create(this, R.drawable.rocket_anim)

        animText()
        avdRocket?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                super.onAnimationEnd(drawable)
                openNextScreen()
            }
        })

        ivAnimRocket.setImageDrawable(avdRocket)
        avdRocket?.start()
    }

    override fun onBackPressed() {

    }
}