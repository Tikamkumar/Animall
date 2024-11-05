package com.online.animall.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.online.animall.R
import com.online.animall.data.model.BuyAnimalModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class BuyAnimalAdapter(private val context: Context, private val list: MutableList<BuyAnimalModel>, private val isAllAnimal: Boolean): RecyclerView.Adapter<BuyAnimalAdapter.ViewHolder>() {

    private var mViewPagerAdapter: SlideImageAdapter? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val time: TextView = itemView.findViewById(R.id.time)
        val allAnimalLayout: LinearLayout = itemView.findViewById(R.id.all_user_lay)
        val delete: Button = itemView.findViewById(R.id.delete_btn)
        val viewPager: ViewPager = itemView.findViewById(R.id.view_pager)
        val icPhone : ImageView = itemView.findViewById(R.id.ic_phone)
        val icWhatsapp : ImageView = itemView.findViewById(R.id.ic_whatsapp)
        val name : TextView = itemView.findViewById(R.id.name)
        val userPic : ImageView = itemView.findViewById(R.id.userPic)
        val negotiable : ImageView = itemView.findViewById(R.id.negotiable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Toast.makeText(context, "Create Vie Holder", Toast.LENGTH_SHORT).show()
        val view = LayoutInflater.from(parent.context).inflate(R.layout.buy_animal_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Toast.makeText(context, "Bind Vie Holder", Toast.LENGTH_SHORT).show()

        val item = list[position]
        if(isAllAnimal)
          holder.allAnimalLayout.visibility = View.VISIBLE
        else
            holder.delete.visibility = View.VISIBLE

        holder.name.text = item.userName
        holder.icWhatsapp.setOnClickListener {
            openWhatsapp(item.phoneNo)
        }
        holder.icPhone.setOnClickListener {
            makeCall(item.phoneNo)
        }
        if(item.isNegotiable == "true") {
            holder.negotiable.visibility = View.VISIBLE
        }
        if(item.userPic.isNotEmpty()) {
            Picasso.get()
                .load("http://192.168.1.12:5001/uploads/${item.userPic}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(holder.userPic)
        }
        mViewPagerAdapter = SlideImageAdapter(context, item.images)
        holder.viewPager.adapter = mViewPagerAdapter
  }

    private fun getTime(timestamp: String): String {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC") // Set time zone to UTC
        }.parse(timestamp)

        // Format the date to "10 Oct 2024"
        val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = outputFormatter.format(date)

        return formattedDate
    }

    private fun openWhatsapp(phoneNo: String) {
        try {
            val url = "https://wa.me/+91${phoneNo}?text="
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (exp: Exception) {
            Toast.makeText(context, "WhatsApp is not installed...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeCall(phoneNo: String) {
        val iCall = Intent(Intent.ACTION_DIAL)
        iCall.data = Uri.parse("tel:${phoneNo}")
        context.startActivity(iCall)
    }
}