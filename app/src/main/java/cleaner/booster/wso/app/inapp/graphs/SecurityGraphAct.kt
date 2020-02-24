package cleaner.booster.wso.app.inapp.graphs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.common.analytics.Events

class SecurityGraphAct : ParentAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.security_graph_act)
        Events.logSecurity()
    }
}