package cleaner.booster.wso.app.inapp.premiums

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cleaner.booster.wso.app.MainActivity
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.SubscriptionProvider
import cleaner.booster.wso.app.common.analytics.Events
import cleaner.booster.wso.app.common.tests.ABConfig
import kotlinx.android.synthetic.main.diamond_act.view.*
import kotlinx.android.synthetic.main.fragment_buy_consume.*

class DiamondFrag : Fragment(R.layout.diamond_act) {

    companion object{
        private const val TAG_FROM = "TAG_FROM"

        fun newInstance(from : String) : DiamondFrag{
            var bundle = Bundle()
            bundle.putString(TAG_FROM, from)
            var fragment = DiamondFrag()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val from = arguments?.getString(TAG_FROM)
        val abVersion = activity?.getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE)?.getString(ABConfig.KEY_FOR_SAVE_STATE, "")
        view.btnPay.setOnClickListener { View.OnClickListener { _ ->
            activity?.let { it1 -> SubscriptionProvider.startChoiseSub(it1, abVersion!!) }
        } }

        view.tvNext.setOnClickListener { _ ->
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
        Events.logOpenPrem(from!!)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && activity is MainActivity) {
            MainActivity.setInfo(R.string.remove_ads)
        }
    }
}