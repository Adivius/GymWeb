package de.adivius.gymweb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import java.util.*

class HW_Input : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hw_input)
        supportActionBar!!.title = "Hinzufügen"

        listtheSubs(this)


        val c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))
        val year = c.get(Calendar.YEAR)
        var month = if ((c.get(Calendar.MONTH) + 1).toString().length == 1) {
            "0${(c.get(Calendar.MONTH) + 1)}"
        } else {
            (c.get(Calendar.MONTH) + 1).toString()
        }
        var day = if (c.get(Calendar.DAY_OF_MONTH).toString().length == 1) {
            "0${c.get(Calendar.DAY_OF_MONTH)}"
        } else {
            c.get(Calendar.DAY_OF_MONTH).toString()
        }
        findViewById<AutoCompleteTextView>(R.id.SelectDay).setText("" + day + "." + month + "." + year)

        var subs = ArrayList<String>()

        for (i in Subjects) subs.add(i.name)
        subs.add("Hinzufügen")
        val subjectAdapter = ArrayAdapter(this, R.layout.list_subject, subs)
        findViewById<AutoCompleteTextView>(R.id.SelectSubject).setAdapter(subjectAdapter)

        findViewById<TextInputLayout>(R.id.Layout3).setOnClickListener {
            selectDate(this, findViewById(R.id.SelectDay))
        }
        findViewById<AutoCompleteTextView>(R.id.SelectDay).setOnClickListener {
            selectDate(this, findViewById(R.id.SelectDay))
        }
        findViewById<TextInputLayout>(R.id.Layout3).setStartIconOnClickListener {
            selectDate(this, findViewById(R.id.SelectDay))
        }



        //done button self
        val viewer = findViewById<View>(R.id.save_btn)
        var ca = true
        viewer.setOnClickListener {
            if (findViewById<AutoCompleteTextView>(R.id.SelectSubject).text.toString() == "Hinzufügen") { startActivity(Intent(this, SubjectManagement::class.java)); finish() ; return@setOnClickListener }
            if (findViewById<TextInputEditText>(R.id.Task).text.toString() == "") { toast("Bitte fülle das Aufgaben-Feld aus"); return@setOnClickListener }
            if (!ca) return@setOnClickListener
            ca = false
            animation()
        }
    }


    fun animation(){
        val viewer = findViewById<View>(R.id.save_btn)
        val save_btn_click = ProgressButton(this, viewer)
        save_btn_click.buttonActivated()
        Handler().postDelayed({
            save_btn_click.buttonFinished()
            Handler().postDelayed({
                uploadTasks()
                finish()
            }, 1000)
        }, 2000)
    }


    private fun uploadTasks(){
        val subject = findViewById<AutoCompleteTextView>(R.id.SelectSubject).text.toString()
        val task = findViewById<TextInputEditText>(R.id.Task).text.toString()
        val day = findViewById<AutoCompleteTextView>(R.id.SelectDay).text.toString()

        var hwi = Homework("$subject", "$task", "$day")
        HomeArray.add(hwi)

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