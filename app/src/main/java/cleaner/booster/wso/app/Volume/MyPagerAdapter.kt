package cleaner.booster.wso.app.Volume

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import cleaner.booster.wso.app.MyApp
import cleaner.booster.wso.app.common.analytics.EventProperties
import cleaner.booster.wso.app.common.tests.ABConfig
import cleaner.booster.wso.app.inapp.premiums.DiamondFrag
import cleaner.booster.wso.app.inapp.premiums.LineFrag

/**
 * Created by intag pc on 2/12/2017.
 */

class MyPagerAdapter(fm: FragmentManager, internal var mNumOfTabs: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                return PhoneBoosterFrag()
            }
            1 -> {
                return BatterySaverFrag()
            }
            2 -> {
                return CPUCoolerFrag()
            }
            3 -> {
                return JunkCleanerFrag()
            }
            else -> {
                return getPremFragment()
            }
        }
    }

    private fun getPremFragment(): Fragment {
        val version = MyApp.getInstance().getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, Context.MODE_PRIVATE).getString(ABConfig.KEY_FOR_SAVE_STATE, "")
        return when(version){
            ABConfig.DEFAULT, ABConfig.A, ABConfig.D, ABConfig.F -> DiamondFrag.newInstance(EventProperties.from_app)
            ABConfig.B, ABConfig.C, ABConfig.E, ABConfig.G -> LineFrag.newInstance(EventProperties.from_app)
            else -> LineFrag.newInstance(EventProperties.from_app_error_version)
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
