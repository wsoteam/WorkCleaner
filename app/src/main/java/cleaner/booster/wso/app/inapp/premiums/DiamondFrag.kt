package cleaner.booster.wso.app.inapp.premiums

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cleaner.booster.wso.app.Config
import cleaner.booster.wso.app.MainActivity
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.SubscriptionProvider
import cleaner.booster.wso.app.common.analytics.Events
import cleaner.booster.wso.app.common.tests.ABConfig
import cleaner.booster.wso.app.inapp.InAppCallback
import kotlinx.android.synthetic.main.diamond_act.view.*
import kotlinx.android.synthetic.main.fragment_buy_consume.*

class DiamondFrag : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.diamond_act, container, false)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val from = arguments?.getString(TAG_FROM)
        val abVersion = activity?.getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, MODE_PRIVATE)?.getString(ABConfig.KEY_FOR_SAVE_STATE, "")
        view.btnPay.setOnClickListener { _ ->
            activity?.let { it1 -> SubscriptionProvider.startChoiseSub(it1, abVersion!!, object : InAppCallback{
                override fun trialSucces() {
                    handlInApp()
                }
            }) }
        }

        view.tvNext.setOnClickListener { _ ->
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
        Events.logOpenPrem(from!!)
        if (activity is MainActivity){
            view.tvNext.visibility = View.INVISIBLE
        }
    }

    private fun handlInApp() {
        Events.logPurschase(arguments?.getString(TAG_FROM).toString())
        SubscriptionProvider.setSuccesSubscription()
        activity?.let {
            startActivity(Intent(it, MainActivity::class.java))
        }
        activity?.finish()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && activity is MainActivity) {
            MainActivity.setInfo(R.string.remove_ads)
        }
    }
}