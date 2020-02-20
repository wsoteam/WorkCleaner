package cleaner.booster.wso.app.inapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import cleaner.booster.wso.app.R
import kotlinx.android.synthetic.main.rocket_act.ivAnimRocket

class RocketAct : AppCompatActivity(R.layout.rocket_act) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var avdRocket = AnimatedVectorDrawableCompat.create(this, R.drawable.rocket_anim)
    avdRocket?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback(){
      override fun onAnimationEnd(drawable: Drawable?) {
        super.onAnimationEnd(drawable)
        openNextScreen()
      }
    })

    ivAnimRocket.setImageDrawable(avdRocket)
    avdRocket?.start()
  }

  private fun openNextScreen() {

  }
}