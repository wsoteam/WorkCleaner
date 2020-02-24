package cleaner.booster.wso.app.inapp.premiums

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cleaner.booster.wso.app.MainActivity
import cleaner.booster.wso.app.R
import cleaner.booster.wso.app.SubscriptionProvider
import kotlinx.android.synthetic.main.diamond_act.view.*

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
        val abVersion = arguments?.getString(TAG_FROM)
        view.btnPay.setOnClickListener { View.OnClickListener { _ ->
            activity?.let { it1 -> SubscriptionProvider.startChoiseSub(it1, abVersion!!) }
        } }

        view.tvNext.setOnClickListener { _ ->
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }
}