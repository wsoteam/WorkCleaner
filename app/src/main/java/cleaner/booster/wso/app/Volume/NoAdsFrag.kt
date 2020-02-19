package cleaner.booster.wso.app.Volume

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import cleaner.booster.wso.app.MainActivity
import cleaner.booster.wso.app.R

import kotlinx.android.synthetic.main.fragment_buy_consume.*
import cleaner.booster.wso.app.SubscriptionProvider


class NoAdsFrag : AppCompatActivity() {
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_fragment)
        if (savedState == null) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                    .add(R.id.fragment, MyFragment())
                    .commit()
        }
    }

    class MyFragment : Fragment() {
        private var resCode = 0
        private val canClick = MutableLiveData<Boolean>()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View? {
            var view = inflater.inflate(R.layout.fragment_buy_consume, container, false)
            return view
        }



        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            abnb_no_ad.setAnimation("12937-gift-box.json")
            abnb_no_ad.loop(false)
            canClick.observe(this, Observer {
                buy_consume.isEnabled = it
            })

            buy_consume.setOnClickListener {
                SubscriptionProvider.startSubscription(activity!!)
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            resCode = resultCode
        }


        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            if (isVisibleToUser) {
                abnb_no_ad.playAnimation()
                MainActivity.setInfo(R.string.remove_ads)
            } else {
            }
        }

        companion object {
            private val AD_FREE = "noads"
        }
    }

}


