package de.adivius.gymweb

import android.content.Context
import android.widget.TextView
import com.google.gson.Gson
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

var JobArray = ArrayList<Job>()

class Job(val subject: String, val day: String, val type: String, val topic: String) {
    constructor() : this("", "", "", "")
}

class JobItem(var jobitem: Job) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.list_job

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.job_list_subject).text = jobitem.subject
        viewHolder.itemView.findViewById<TextView>(R.id.job_list_date).text = "am: ${jobitem.day}"
        viewHolder.itemView.findViewById<TextView>(R.id.job_list_type).text = jobitem.type
        viewHolder.itemView.findViewById<TextView>(R.id.job_list_topic).text = "Thema: ${jobitem.topic}"
    }
}

fun Context.addJob(name: String, day: String, type: String, topic: String){
    var job = Job(name, day, type, topic)
    JobArray.add(job)

    val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    val gson = Gson()
    var json = gson.toJson(JobArray)
    editor.putString("jobs", json)
    editor.apply()
}