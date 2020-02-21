package cleaner.booster.wso.app.common.analytics

import android.util.Log
import com.amplitude.api.Amplitude
import com.amplitude.api.Identify

class UserProperties {
  companion object{
    const val ab_version = "ab_version"

    fun setABUserProp(testValue : String){
      Log.e("LOL", testValue)
      var identify = Identify().set(ab_version, testValue)
      Amplitude.getInstance().identify(identify)
    }
  }
}