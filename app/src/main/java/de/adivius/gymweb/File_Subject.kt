package de.adivius.gymweb

import android.content.Context
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

var Subjects = ArrayList<Subject>()

class Subject(val name: String){
    constructor(): this("")
}

class SubjectItem(var subitem: Subject) : Item<GroupieViewHolder>() {

    override fun getLayout() = R.layout.list_sub

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.ShowName).text = subitem.name
    }
}

fun addSubject(context: Context, name: String){
    var sub = Subject(name)
    Subjects.add(sub)

    val sharedPref = context.getSharedPreferences("Devs", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    val gson = Gson()
    var json = gson.toJson(Subjects)
    editor.putString("subjects", json)
    editor.apply()
}

fun listtheSubs(context: Context) {
    val sharedPref = context.getSharedPreferences("Devs", Context.MODE_PRIVATE)
    val gson = Gson()
    var json = sharedPref.getString("subjects", null)
    val type = object : TypeToken<ArrayList<Subject>>() {}.type
    Subjects = gson.fromJson<ArrayList<Subject>>(json, type) ?: return
}