package cleaner.booster.wso.app.inapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import cleaner.booster.wso.app.R
import kotlinx.android.synthetic.main.rocket_act.ivAnimRocket
import kotlinx.android.synthetic.main.rocket_act.tvAnimText

class RocketAct : AppCompatActivity(R.layout.rocket_act) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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

  private fun animText() {
    var counter = 1
    object : CountDownTimer(14000, 1000) {
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
        } else if(counter == 4 || counter == 7 || counter == 10){
          tvAnimText.text = resources.getText(R.string.ab_optimization)
        }
        counter ++
      }
    }.start()
  }

  private fun openNextScreen() {
      finish()
  }
}