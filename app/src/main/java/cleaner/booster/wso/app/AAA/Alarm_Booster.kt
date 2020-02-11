package cleaner.booster.wso.app.AAA

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.Volume.PhoneBoosterFrag
import cleaner.booster.wso.app.utils.PreferencesProvider

/**
 * Created by intag pc on 3/2/2017.
 */

class Alarm_Booster : BroadcastReceiver() {

    //todo Prefs
    override fun onReceive(context: Context, intent: Intent) {

        PreferencesProvider.getInstance().edit()
                .putString("booster", "1")
                .apply()

        try {
            PhoneBoosterFrag.setButtonText(R.string.optimize)
        } catch (e: Exception) {

        }

    }
}
