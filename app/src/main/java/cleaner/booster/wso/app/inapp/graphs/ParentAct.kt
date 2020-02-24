package cleaner.booster.wso.app.inapp.graphs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.inapp.premiums.PremiumHostAct
import kotlinx.android.synthetic.main.battery_graph_act.*

open class ParentAct : AppCompatActivity(R.layout.battery_graph_act) {

    override fun onResume() {
        super.onResume()
        btnNext.setOnClickListener { _ ->
            startActivity(Intent(this, PremiumHostAct::class.java))
            finish()
        }
    }

    override fun onBackPressed() {

    }
}