package cleaner.booster.wso.app

import com.google.android.gms.ads.identifier.AdvertisingIdClient

interface AdvertisingInfoListener {

    fun onInfoReceived(info: AdvertisingIdClient.Info)

}