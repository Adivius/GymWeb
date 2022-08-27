package de.adivius.gymweb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class Description : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        val subject = intent.getStringExtra("subject")
        val day = intent.getStringExtra("day")
        val task = intent.getStringExtra("task")

        Log.d("Devs", "Hausaufgabe $task in $subject muss bis $day erledigt werden!! ")

        findViewById<TextView>(R.id.des_subject).text = subject
        findViewById<TextView>(R.id.des_task).text = task
        findViewById<TextView>(R.id.des_day).text = day

        findViewById<TextView>(R.id.des_days).text = days(day!!)

        findViewById<TextView>(R.id.des_date).text = today()

        findViewById<Button>(R.id.des_delete).setOnClickListener {
            deleteTask()
        }
    }
    fun deleteTask() {
        val pos = intent.getStringExtra("pos")!!.toInt()
        HomeArray.removeAt(pos)
        val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        var json = gson.toJson(HomeArray)
        editor.putString("tasks", json)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}