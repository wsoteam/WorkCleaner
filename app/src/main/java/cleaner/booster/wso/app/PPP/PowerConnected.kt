package cleaner.booster.wso.app.PPP

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import cleaner.booster.wso.app.MainActivity

/**
 * Created by intag pc on 3/14/2017.
 */

class PowerConnected : BroadcastReceiver() {

    // bROAD CAST THAT lISTEN fOR charger Connected Events

    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }
}
