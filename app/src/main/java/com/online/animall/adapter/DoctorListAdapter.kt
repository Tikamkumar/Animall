package com.online.animall.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.online.animall.R
import com.online.animall.data.model.DoctorModel
import com.squareup.picasso.Picasso

class DoctorListAdapter(var doctorList: MutableList<DoctorModel>, private var context: Context): RecyclerView.Adapter<DoctorListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val pic = itemView.findViewById<ImageView>(R.id.doctor_pic)
        val ic_phone = itemView.findViewById<ImageView>(R.id.ic_phone)
        val ic_whatsApp = itemView.findViewById<ImageView>(R.id.ic_whatsapp)
        val name = itemView.findViewById<TextView>(R.id.name)
        val profession = itemView.findViewById<TextView>(R.id.profession)
        val expAndEducation = itemView.findViewById<TextView>(R.id.education_experience)
        val address = itemView.findViewById<TextView>(R.id.address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.doctor_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = doctorList.size

    override fun onBindViewHolder(holder: DoctorListAdapter.ViewHolder, position: Int) {
        val doctor = doctorList[position]
        if(doctor.pic == "") {
            Picasso.get()
                .load("http://192.168.1.12:5001/uploads/${doctor.pic}")
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(holder.pic)
        }
        holder.name.text = doctor.name
        holder.profession.text = doctor.profession
        holder.expAndEducation.text = doctor.education + "\n" + doctor.experience + " ${context.getString(R.string.year_experience)}"
        holder.address.text = doctor.address
        holder.ic_whatsApp.setOnClickListener {
            if(doctor.whatsNo.isEmpty())
                Toast.makeText(context, "WhatsApp No not available", Toast.LENGTH_SHORT).show()
            else
                openWhatsapp(doctor.whatsNo)
        }
        holder.ic_phone.setOnClickListener {
                makeCall(doctor.phoneNo)
        }
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