package cleaner.booster.wso.app.inapp.premiums

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.line_act.view.*

class LineFrag : Fragment() {

    companion object{
        private const val TAG_FROM = "TAG_FROM"

        fun newInstance(from : String) : LineFrag{
            var bundle = Bundle()
            bundle.putString(TAG_FROM, from)
            var fragment = LineFrag()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.line_act, container, false)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val from = arguments?.getString(TAG_FROM)
        val abVersion = activity?.getSharedPreferences(ABConfig.KEY_FOR_SAVE_STATE, Context.MODE_PRIVATE)?.getString(ABConfig.KEY_FOR_SAVE_STATE, "")
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
        activity!!.getSharedPreferences(Config.HAS_SUBSCRIPTION, Context.MODE_PRIVATE).edit().putBoolean(Config.HAS_SUBSCRIPTION, true).commit()
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