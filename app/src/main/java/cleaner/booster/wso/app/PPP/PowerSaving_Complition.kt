package cleaner.booster.wso.app.PPP

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.animation.AccelerateInterpolator
import android.widget.Toast

import cleaner.booster.wso.app.AdMobFullscreenManager
import cleaner.booster.wso.app.R

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent

import cleaner.booster.wso.app.Constants.adsShow
import kotlinx.android.synthetic.main.phone_booster.dynamicArcView2
import kotlinx.android.synthetic.main.powersaving_completion.*

class PowerSaving_Complition : Activity(), AdMobFullscreenManager.AdMobFullscreenDelegate {

  internal var check = 0
  private var fullscreenManager: AdMobFullscreenManager? = null
  private var mAdView: AdView? = null

  private val adManager: AdMobFullscreenManager?
    get() {
      if (fullscreenManager == null) {
        configureManager()
      }
      return fullscreenManager
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.powersaving_completion)
    //TODO ban
    /*mAdView = findViewById(R.id.adView)
    val adRequest = AdRequest.Builder()
        .build()
    mAdView!!.loadAd(adRequest)*/

    dynamicArcView2.addSeries(SeriesItem.Builder(Color.argb(255, 218, 218, 218))
        .setRange(0f, 100f, 0f)
        .setInterpolator(AccelerateInterpolator())
        .build())

    dynamicArcView2.addSeries(SeriesItem.Builder(Color.parseColor("#00ca71"))
        .setRange(0f, 100f, 100f)
        .setInitialVisibility(false)
        .setLineWidth(32f)
        .build())

    val seriesItem2 = SeriesItem.Builder(Color.parseColor("#00ca71"))
        .setRange(0f, 100f, 0f)
        .setLineWidth(32f)
        .build()

    val series1Index2 = dynamicArcView2.addSeries(seriesItem2)

    setAnim()

    seriesItem2.addArcSeriesItemListener(object : SeriesItem.SeriesItemListener {
      override fun onSeriesItemAnimationProgress(
        v: Float,
        v1: Float
      ) {
        val i = v1.toInt()
        completion.text = "$i%"

        if (v1 >= 1 && v1 <= 5) {
          abnb_1.playAnimation()
        } else if (v1 >= 10 && v1 < 40) {
          ist.setTextColor(Color.parseColor("#00ca71"))
        } else if (v1 >= 40 && v1 < 50) {
          abnb_2.playAnimation()
        } else if (v1 >= 50 && v1 < 65) {
          sec.setTextColor(Color.parseColor("#00ca71"))
        } else if (v1 >= 65 && v1 < 75) {
          abnb_3.playAnimation()
        } else if (v1 >= 75 && v1 < 85) {
          thi.setTextColor(Color.parseColor("#00ca71"))
        } else if (v1 >= 85 && v1 < 90) {
          abnb_4.playAnimation()
        } else if (v1 >= 90 && v1 <= 100) {
          fou.setTextColor(Color.parseColor("#00ca71"))
        }
      }

      override fun onSeriesItemDisplayProgress(v: Float) {
      }
    })

    dynamicArcView2.addEvent(
        DecoEvent.Builder(100f).setIndex(series1Index2).setDelay(1000).setListener(object :
            DecoEvent.ExecuteEventListener {
          override fun onEventStart(decoEvent: DecoEvent) {

          }

          override fun onEventEnd(decoEvent: DecoEvent) {
            if (adsShow) {
              adManager!!.completed()
            } else {
              youDesirePermissionCode(this@PowerSaving_Complition)
              closesall()
              check = 1
            }
          }
        }).build()
    )
  }

  private fun setAnim() {
    abnb_1.setAnimation("433-checked-done.json")
    abnb_2.setAnimation("433-checked-done.json")
    abnb_3.setAnimation("433-checked-done.json")
    abnb_4.setAnimation("433-checked-done.json")
  }

  fun closesall() {
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    if (mBluetoothAdapter.isEnabled) {
      mBluetoothAdapter.disable()
    }
    ContentResolver.setMasterSyncAutomatically(false)
  }

  override fun onBackPressed() {
    //        super.onBackPressed();
  }

  fun youDesirePermissionCode(context: Activity) {
    val permission: Boolean
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      permission = Settings.System.canWrite(context)
    } else {
      permission = ContextCompat.checkSelfPermission(
          context, Manifest.permission.WRITE_SETTINGS
      ) == PackageManager.PERMISSION_GRANTED
    }
    if (permission) {
      //do your code
      Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
      setAutoOrientationEnabled(context, false)

      finish()
    } else {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivityForResult(intent, 1)
      } else {
        ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_SETTINGS), 1)
      }
    }
  }

  //
  @SuppressLint("NewApi")
  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == 1 && Settings.System.canWrite(this)) {
      Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
      setAutoOrientationEnabled(this, false)
      finish()
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Toast.makeText(applicationContext, "onRequestPermissionsResult", Toast.LENGTH_LONG)
          .show()
      Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
      setAutoOrientationEnabled(this, false)
      finish()
    }
  }

  override fun onResume() {
    super.onResume()
    if (check == 1) {
      try {
        Settings.System.putInt(this.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 30)
        setAutoOrientationEnabled(this, false)
      } catch (e: Exception) {
        finish()
      }
      finish()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  private fun configureManager() {
    if (fullscreenManager == null) {
      fullscreenManager = AdMobFullscreenManager(this, this)
    } else {
      fullscreenManager!!.reloadAd()
    }
  }

  override fun ADLoaded() {
    if (adManager!!.tryingShowDone) {
      adManager!!.showAdd()
    }
  }

  override fun ADIsClosed() {
    if (adManager!!.tryingShowDone) {
      youDesirePermissionCode(this@PowerSaving_Complition)
      closesall()
      check = 1
    }
  }

  companion object {
    fun setAutoOrientationEnabled(
      context: Context,
      enabled: Boolean
    ) {
      Settings.System.putInt(
          context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, if (enabled) 1 else 0
      )
    }
  }
}
