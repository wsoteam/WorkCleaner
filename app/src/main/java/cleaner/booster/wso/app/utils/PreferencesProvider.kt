package cleaner.booster.wso.app.utils

import android.content.Context
import cleaner.booster.wso.app.MyApp

object PreferencesProvider{

    private val preferences = MyApp.getInstance().getSharedPreferences("waseem", Context.MODE_PRIVATE)

    fun getInstance() = preferences
}