package cleaner.booster.wso.app.inapp.premiums

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cleaner.booster.wso.app.Config
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.common.tests.ABConfig

class PremiumHostAct : AppCompatActivity(R.layout.premium_host_activity) {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    var abVersion = getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, Context.MODE_PRIVATE).getString(ABConfig.KEY_FOR_SAVE_STATE, "")
    when(abVersion){
      ABConfig.DEFAULT, ABConfig.A, ABConfig.D, ABConfig.F -> startDiamondPrem()
      ABConfig.B, ABConfig.C, ABConfig.E, ABConfig.G -> startLinePrem()
    }
  }

  private fun startLinePrem() {
    var fm = supportFragmentManager.beginTransaction()
    fm.add(R.id.flHost, LineFrag.newInstance(intent.getStringExtra(Config.PREM_FROM))).commit()
  }

  private fun startDiamondPrem() {
    var fm = supportFragmentManager.beginTransaction()
    fm.add(R.id.flHost, DiamondFrag.newInstance(intent.getStringExtra(Config.PREM_FROM))).commit()
  }

  override fun onBackPressed() {

  }
}