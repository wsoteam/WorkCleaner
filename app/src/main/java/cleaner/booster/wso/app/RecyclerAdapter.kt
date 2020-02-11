package cleaner.booster.wso.app

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import cleaner.booster.wso.app.OOP.ApplicationsClass

/**
 * Created by intag pc on 2/16/2017.
 */

class RecyclerAdapter(var apps: List<ApplicationsClass>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_apps, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val app = apps[position]
        holder.size.text = app.size
        holder.image.setImageDrawable(app.image)
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var size: TextView
        internal var image: ImageView

        init {
            size = view.findViewById<View>(R.id.apptext) as TextView
            image = view.findViewById<View>(R.id.appimage) as ImageView

        }
    }
}
