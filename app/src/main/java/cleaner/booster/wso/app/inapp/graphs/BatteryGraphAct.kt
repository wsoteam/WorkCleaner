package cleaner.booster.wso.app.inapp.graphs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cleaner.booster.wso.app.R

class BatteryGraphAct : ParentAct() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.battery_graph_act)
    }
}