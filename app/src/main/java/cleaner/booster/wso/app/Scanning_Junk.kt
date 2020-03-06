package cleaner.booster.wso.app

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

import cleaner.booster.wso.app.BCJ.Junk_Apps_Adapter
import cleaner.booster.wso.app.OOP.ApplicationsClass

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import java.util.ArrayList
import java.util.Timer
import java.util.TimerTask

import cleaner.booster.wso.app.service.CleanService

import cleaner.booster.wso.app.Constants.adsShow
import kotlinx.android.synthetic.main.scanning_junk.*

class Scanning_Junk : Activity(), AdMobFullscreenManager.AdMobFullscreenDelegate {

  internal var check = 0
  internal var packages: List<ApplicationInfo> = listOf()
  internal var prog = 0
  lateinit var T2: Timer
  internal var mAdapter: Junk_Apps_Adapter? = null
  var apps: MutableList<ApplicationsClass> = mutableListOf()

  private var fullscreenManager: AdMobFullscreenManager? = null
  private var mAdView: AdView? = null

  private var handler: Handler? = null
  private var junk: Bundle? = null

  private val adManager: AdMobFullscreenManager?
    get() {
      if (fullscreenManager == null) {
        configureManager()
      }
      return fullscreenManager
    }

  private fun startFinishAnim() {
    val anim =
      AnimatorInflater.loadAnimator(applicationContext, R.animator.flipping) as ObjectAnimator
    anim.target = front
    anim.duration = 3000
    anim.start()

    anim.addListener(object : Animator.AnimatorListener {
      override fun onAnimationStart(animation: Animator) {

      }

      override fun onAnimationEnd(animation: Animator) {
        if (adsShow) {
          adManager!!.completed()
        } else {
          val handler7 = Handler()
          handler7.postDelayed({
            val intent = Intent(this@Scanning_Junk, MainActivity::class.java)
            intent.putExtra("frag", 3)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
          }, 1000)
        }
      }

      override fun onAnimationCancel(animation: Animator) {
      }

      override fun onAnimationRepeat(animation: Animator) {

      }
    })
    files.text = ""
  }

  private fun startMainAnimation() {
    //TODO ban

    /*mAdView = findViewById(R.id.adView)
    val adRequest = AdRequest.Builder()
        .build()
    mAdView!!.loadAd(adRequest)*/
    apps = ArrayList()

    val rotate =
      RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    rotate.duration = 1500
    rotate.repeatCount = 4
    rotate.interpolator = LinearInterpolator()

    rotate.setAnimationListener(object : Animation.AnimationListener {
      override fun onAnimationStart(animation: Animation) {

      }

      override fun onAnimationEnd(animation: Animation) {
        T2.cancel()
        T2.purge()
        handler!!.sendEmptyMessage(END_ANIMATION)
      }

      override fun onAnimationRepeat(animation: Animation) {
        check++
        startAnim(check)
      }
    })
    front.startAnimation(rotate)

    packages = packageManager.getInstalledApplications(0)
    val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    T2 = Timer()
    T2.scheduleAtFixedRate(object : TimerTask() {
      override fun run() {
        runOnUiThread {
          if (prog < packages.size) {
            files.text = "" + packages[prog].sourceDir
            prog++
          } else {
            T2.cancel()
            T2.purge()
          }
        }
      }
    }, 80, 80)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    junk = intent.extras
    setContentView(R.layout.scanning_junk)
    abnb_del.setAnimation("12818-file-recover.json")
    abnb_del.loop(true)
    abnb_del.playAnimation()
    handler = object : Handler() {
      override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

        when (msg.what) {
          END_ANIMATION ->
            //TODO: if android <= Nougat startService
            if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
              val i = Intent(this@Scanning_Junk, CleanService::class.java)
              i.putExtra("junk", junk!!.getString("junk"))
              this@Scanning_Junk.startService(i)
            } else {
              handler!!.sendEmptyMessage(END_CLEAN)
            }
          END_CLEAN -> startFinishAnim()
        }
      }
    }
    if (intent.getBooleanExtra("isScanned", false)) {
      handler!!.sendEmptyMessage(END_CLEAN)
    } else {
      startMainAnimation()
    }
  }

  internal fun startAnim(i1: Int) {
  }

  fun add(
    text: String,
    position: Int
  ) {
    val p = 0 + (Math.random() * (packages.size - 1 - 0 + 1)).toInt()
    var ico: Drawable? = null
    val packageName = packages[p].packageName
    var item = try {
      val pName = packageManager.getApplicationLabel(
          packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
      ) as String
      val a = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
      ico = packageManager.getApplicationIcon(packages[p].packageName)
      ApplicationsClass(packages[p].dataDir, ico)

    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
      ApplicationsClass(packages[p].dataDir, null)
    }

    apps.add(item)
    mAdapter!!.notifyItemInserted(position)
  }

  override fun onBackPressed() {
    //        super.onBackPressed();
  }

  private fun configureManager() {
    if (fullscreenManager == null) {
      fullscreenManager = AdMobFullscreenManager(this, this)
    } else {
      fullscreenManager!!.reloadAd()
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    junk = savedInstanceState
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString("junk", junk!!.getString("junk"))
    super.onSaveInstanceState(outState)
  }

  override fun ADLoaded() {
    if (adManager!!.tryingShowDone) {
      adManager!!.showAdd()
    }
  }

  override fun ADIsClosed() {
    if (adManager!!.tryingShowDone) {

      val handler7 = Handler()
      handler7.postDelayed({
        val intent = Intent(this@Scanning_Junk, MainActivity::class.java)
        intent.putExtra("frag", 3)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
      }, 1000)
    }
  }

  companion object {
    private val END_ANIMATION = 1
    private val END_CLEAN = 3
  }
}
