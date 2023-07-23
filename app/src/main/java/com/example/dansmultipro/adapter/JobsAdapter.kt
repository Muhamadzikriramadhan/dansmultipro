package com.example.dansmultipro.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dansmultipro.R
import com.example.dansmultipro.model.ListJob
import com.squareup.picasso.Picasso

class JobsAdapter(private var list: ArrayList<ListJob>, private val listener: ListenerClickJob):
    RecyclerView.Adapter<JobsAdapter.JobViewHolder>() {

    fun getFilter(listJob: ArrayList<ListJob>) {
        list = listJob
        notifyDataSetChanged()
    }

    class JobViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_job, parent, false)){
        private var nameCorporate: TextView? = null
        private var nameJob: TextView? = null
        private var locationCorporate: TextView? = null
        private var iconCorporate: ImageView? = null
        private var listeners: ListenerClickJob? = null
        private var click: LinearLayout? = null
        init {
            nameJob = itemView.findViewById(R.id.job)
            nameCorporate = itemView.findViewById(R.id.corporateName)
            locationCorporate = itemView.findViewById(R.id.corporateLocation)
            iconCorporate = itemView.findViewById(R.id.imageCompany)
            click = itemView.findViewById(R.id.layouttodetail)
        }

        fun bind(data: ListJob, listener: ListenerClickJob) {
            nameJob!!.text = data.title
            locationCorporate!!.text = data.location
            nameCorporate!!.text = data.company
            listeners = listener
            Picasso.get()
                .load(data.companyLogo)
                .placeholder(R.drawable.baseline_image_24)
                .into(iconCorporate)
            itemView.setOnClickListener {
                listeners!!.clickek(adapterPosition)
            }
            click!!.setOnClickListener {
                listeners!!.clickek(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return JobViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListenerClickJob {
        fun clickek(position: Int)
    }
}