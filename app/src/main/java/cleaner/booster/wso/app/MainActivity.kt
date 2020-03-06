package cleaner.booster.wso.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.Preference

import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import com.google.android.gms.ads.AdRequest

import cleaner.booster.wso.app.AAA.AlarmBroadCastReceiver
import cleaner.booster.wso.app.Volume.MyPagerAdapter
import cleaner.booster.wso.app.Volume.NoAdsFrag
import cleaner.booster.wso.app.Volume.PhoneBoosterFrag
import cleaner.booster.wso.app.lang.LanguagesDialog
import cleaner.booster.wso.app.lang.SettingsFragment

import java.util.Calendar

import cleaner.booster.wso.app.Constants.adsShow
import cleaner.booster.wso.app.common.analytics.Events
import cleaner.booster.wso.app.common.remote.RemoteConfig
import cleaner.booster.wso.app.utils.PreferencesProvider
import com.amplitude.api.Amplitude
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.banner_layout.*

class MainActivity : AppCompatActivity(), Preference.OnPreferenceClickListener {

    //todo Prefs
    internal var consent: Boolean = false
    var counter = 0

    val BOOSTER = 0
    val BATTERY = 1
    val FAN = 2
    val CLEAN = 3
    val SUB = 4
    val PAGE_COUNT = 4

    private lateinit var mInterstitialAd: InterstitialAd


    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    private fun loadAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = resources.getString(R.string.interstitial)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadAd()
        Events.logMainScreen()
        data.observe(this, Observer {
            name.setText(it)
        })
        Amplitude.getInstance().logEvent("Run")
        setNotification()

        val adRequest = AdRequest.Builder()
                .build()
        if (!SubscriptionProvider.hasSubscription()) {
            adView!!.loadAd(adRequest)
        }
        consent = intent.getBooleanExtra(CONSENT, false)

        val randomNum = 6 + (Math.random() * 18).toInt()

        val randomNum2 = 6 + (Math.random() * 18).toInt()

        val myIntent = Intent(this@MainActivity, AlarmBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, myIntent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val firingCal = Calendar.getInstance()
        val currentCal = Calendar.getInstance()

        firingCal.set(Calendar.HOUR_OF_DAY, randomNum) // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, randomNum2) // Particular minute
        firingCal.set(Calendar.SECOND, 0) // particular second

        var intendedTime = firingCal.timeInMillis
        val currentTime = currentCal.timeInMillis

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(
                    AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent
            )
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1)
            intendedTime = firingCal.timeInMillis

            alarmManager.setRepeating(
                    AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent
            )
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            //Do your own error handling here

            if (oldHandler != null)
                oldHandler.uncaughtException(
                        paramThread,
                        paramThrowable
                ) //Delegates to Android's error handling
            else
                System.exit(2) //Prevents the service/app from freezing
        }


        Log.i("adsShow", adsShow.toString())
        if (SubscriptionProvider.hasSubscription()) {
            tab_layout.menu.removeItem(R.id.nav_rem)
            //tab_layout.addTab(tab_layout.newTab().setIcon(R.drawable.ads))
        }
        val adapter = MyPagerAdapter(supportFragmentManager, tab_layout.maxItemCount)
        pager.adapter = adapter
        pager.offscreenPageLimit = tab_layout.maxItemCount
        pager.beginFakeDrag()

        tab_layout.setOnNavigationItemSelectedListener(object : OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_boost -> pager.currentItem = BOOSTER
                    R.id.nav_battery -> pager.currentItem = BATTERY
                    R.id.nav_fan -> pager.currentItem = FAN
                    R.id.nav_delete -> pager.currentItem = CLEAN
                    R.id.nav_rem -> pager.currentItem = SUB
                }
                return true
            }
        })

        if (intent.getBooleanExtra("fromService", false)) {
            pager.currentItem = 3
        }
        pager.currentItem = intent.getIntExtra("frag", 0)
    }

    private fun firstShow() {
        if (mInterstitialAd.isLoaded && counter == 0) {
            Events.logFirstInter()
            mInterstitialAd.show()
            counter ++
        }
    }

    override fun onResume() {
        super.onResume()
        PreferencesProvider.getInstance()
                .edit()
                .putString("booster", "1")
                .apply()

        try {
            PhoneBoosterFrag.setButtonText(R.string.optimize)
        } catch (e: Exception) {

        }

    }

    private fun setNotification() {
        PreferencesProvider.getInstance()
                .edit()
                .putString("state_Head", resources.getString(R.string.notif_head))
                .putString("state_Body", resources.getString(R.string.notif_body))
                .apply()

        val calendar = Calendar.getInstance()
        val now = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 15)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        //if user sets the alarm after their preferred time has already passed that day
        if (now.after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent =
                PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val alarmManager = getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent
        )


        Log.d("TIME", "setNot")
    }

    override fun onDestroy() {
        super.onDestroy()

        PreferencesProvider.getInstance()
                .edit()
                .putString("button1", "0")
                .putString("button2", "0")
                .putString("button3", "0")
                .putString("button4", "0")
                .putString("button5", "0")
                .apply()
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        return false
    }

    inner class MyException : Exception()// special exception code goes here

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.settings -> {
                val languagesDialog = LanguagesDialog()
                languagesDialog.show(fragmentManager, "LanguagesDialogFragment")
            }
        }//startActivityForResult(new Intent(this, SettingsActivity.class), 1000);
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("LongLogTag")
    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {

        super.onActivityResult(requestCode, resultCode, data)

        Log.d(
                "MainActivity.onActivityResult", "Request code is $requestCode, result code is $resultCode"
        )
        when (requestCode) {
            1000 -> if (resultCode == SettingsFragment.LANGUAGE_CHANGED) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        var fr3: NoAdsFrag.MyFragment? = NoAdsFrag.MyFragment()

        if (fr3 is NoAdsFrag.MyFragment) {
            fr3.onActivityResult(requestCode, resultCode, data)
        }
        // settings instance of Fragment to null
        fr3 = null
        Log.i("FR3", "Fragment: $fr3")
    }

    companion object {
        private val CONSENT = "consent"

        fun getIntent(
                context: Context,
                consent: Boolean
        ): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(CONSENT, consent)
            return intent
        }

        private val data = MutableLiveData<Int>()

        fun setInfo(int: Int) {
            data.postValue(int)
        }
    }
}
