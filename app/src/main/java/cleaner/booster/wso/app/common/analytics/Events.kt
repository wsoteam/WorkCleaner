package cleaner.booster.wso.app.common.analytics

import com.amplitude.api.Amplitude
import org.json.JSONException
import org.json.JSONObject

class Events {
  companion object {
    const val splash_screen = "splash_screen"
    const val rocket_screen = "rocket_screen"
    const val security_screen = "security_screen"

    const val premium_screen = "premium_screen"
    const val premium_screen_from = "from"


    const val main_screen = "main_screen"

    const val trial_success = "trial_success"
    const val trial_success_from = "from"

    const val crash_ab = "crash_ab"
    const val normaly_ab = "normaly_ab"

    fun logError() {
      Amplitude.getInstance()
          .logEvent(crash_ab)
    }

    fun logSuccess() {
      Amplitude.getInstance()
          .logEvent(normaly_ab)
    }

    fun logSplash() {
      Amplitude.getInstance()
          .logEvent(splash_screen)
    }

    fun logRocket() {
      Amplitude.getInstance()
          .logEvent(rocket_screen)
    }

    fun logSecurity() {
      Amplitude.getInstance()
          .logEvent(security_screen)
    }

    fun logMainScreen() {
      Amplitude.getInstance()
          .logEvent(main_screen)
    }

    fun logOpenPrem(from: String) {
      val eventProperties = JSONObject()
      try {
        eventProperties.put(premium_screen_from, from)
      } catch (exception: JSONException) {
        exception.printStackTrace()
      }
      Amplitude.getInstance()
          .logEvent(premium_screen, eventProperties)
    }

    fun logPurschase(from: String) {
      val eventProperties = JSONObject()
      try {
        eventProperties.put(trial_success_from, from)
      } catch (exception: JSONException) {
        exception.printStackTrace()
      }
      Amplitude.getInstance()
          .logEvent(trial_success, eventProperties)
    }
  }
}