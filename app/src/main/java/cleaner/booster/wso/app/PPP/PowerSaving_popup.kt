package cleaner.booster.wso.app.PPP

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import cleaner.booster.wso.app.OOP.PowersClass


import java.util.ArrayList

import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.utils.PreferencesProvider
import kotlinx.android.synthetic.main.powersaving_popup.*

/**
 * Created by intag pc on 2/19/2017.
 */

class PowerSaving_popup : Activity() {
    internal var items: MutableList<PowersClass> = mutableListOf()

    private var mAdView: AdView? = null
    lateinit var mAdapter: PowerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        setContentView(R.layout.powersaving_popup)
        //TODO ban
        /*mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)*/


        items = ArrayList()
        applied.setOnClickListener {
            PreferencesProvider.getInstance().edit()
                    .putString("mode", "1")
                    .apply()

            val i = Intent(applicationContext, PowerSaving_Complition::class.java)
            startActivity(i)

            finish()
        }
        abnb_normal_charge.setAnimation("8577-battery.json")
        abnb_normal_charge.frame = 51
        abnb_normal_charge.loop(false)
        abnb_normal_charge.playAnimation()
    }

    fun add(text: String, position: Int) {
        val item = PowersClass(text)
        items.add(item)
        mAdapter.notifyItemInserted(position)
    }

    override fun onBackPressed() {
        //   super.onBackPressed();
    }

}
