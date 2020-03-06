package cleaner.booster.wso.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import cleaner.booster.wso.app.AAA.Applying_Ultra
import cleaner.booster.wso.app.OOP.PowersClass

import cleaner.booster.wso.app.PPP.PowerAdapter
import kotlinx.android.synthetic.main.ultra_popup.*

class Ultra_PopUp : Activity() {

    lateinit var mAdapter: PowerAdapter
    internal var items: MutableList<PowersClass> = mutableListOf()
    internal var hour: Int = 0
    internal var min: Int = 0
    private var mAdView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        setContentView(R.layout.ultra_popup)
        //TODO ban

       /* mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)*/

        abnb_extreme.setAnimation("13539-sign-for-error-or-explanation-alert.json")
        abnb_extreme.loop(true)
        abnb_extreme.playAnimation()
        try {
            hour = Integer.parseInt(b!!.getString("hour")!!.replace("[^0-9]".toRegex(), "")) - Integer.parseInt(b!!.getString("hournormal")!!.replace("[^0-9]".toRegex(), ""))
            min = Integer.parseInt(b!!.getString("minutes")!!.replace("[^0-9]".toRegex(), "")) - Integer.parseInt(b!!.getString("minutesnormal")!!.replace("[^0-9]".toRegex(), ""))
        } catch (e: Exception) {
            hour = 4
            min = 7
        }
        if (hour == 0 && min == 0) {
            hour = 4
            min = 7
        }

        addedtime.text = "(+" + hour + " h " + Math.abs(min) + " m)"
        addedtimedetail.text = getString(R.string.extended_battery_up_to) + Math.abs(hour) + "h " + Math.abs(min) + "m"

        applied.setOnClickListener {
            val i = Intent(this@Ultra_PopUp, Applying_Ultra::class.java)
            startActivity(i)
            finish()
        }
    }

    fun add(text: String, position: Int) {
        val item = PowersClass(text)
        items.add(item)
        mAdapter.notifyItemInserted(position)

    }

    override fun onBackPressed() {
        // super.onBackPressed();
    }

}
