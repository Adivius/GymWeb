package de.adivius.gymweb

import android.widget.TextView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

var HomeArray = ArrayList<Homework>()

class Homework(val subject: String, val task: String, val day: String) {
    constructor() : this("", "", "")
}

class HomeListItem(var workitem: Homework) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.list_main

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.main_list_subject).text = workitem.subject
        viewHolder.itemView.findViewById<TextView>(R.id.main_list_date).text = days(workitem.day)
    }
}