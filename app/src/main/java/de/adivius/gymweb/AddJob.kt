package de.adivius.gymweb

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddJob : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)
        supportActionBar!!.title = "Hinzufügen"
        doneButton()

        findViewById<AutoCompleteTextView>(R.id.job_date_text).setText("${tDay()}.${tMonth()}.${tYear()}")

        val subs = ArrayList<String>()
        for (i in Subjects) subs.add(i.name)
        subs.add("Hinzufügen")
        val subjectAdapter_subject = ArrayAdapter(this, R.layout.list_subject, subs)
        findViewById<AutoCompleteTextView>(R.id.job_subject_text).setAdapter(subjectAdapter_subject)
        findViewById<TextInputLayout>(R.id.job_date).setStartIconOnClickListener {
            selectDate(this, findViewById(R.id.job_date_text))
        }

        val type = ArrayList<String>()
        type.add("Klausur")
        type.add("Schriftliche Überprüfung")
        type.add("Präsentation")
        type.add("Termin")
        type.add("Anders")

        val subjectAdapter_type = ArrayAdapter(this, R.layout.list_subject, type)
        findViewById<AutoCompleteTextView>(R.id.job_type_text).setAdapter(subjectAdapter_type)
    }


    private fun animation(){
        val viewer = findViewById<View>(R.id.job_save_btn)
        val save_btn_click = ProgressButton(this, viewer)
        save_btn_click.buttonActivated()
        Handler().postDelayed(Runnable {
            save_btn_click.buttonFinished()
            Handler().postDelayed(Runnable {
                val topic: String = if (findViewById<TextInputEditText>(R.id.job_topic_text).text.toString() == "") "Kein Thema" else findViewById<TextInputEditText>(R.id.job_topic_text).text.toString()
                addJob(
                    findViewById<AutoCompleteTextView>(R.id.job_subject_text).text.toString(),
                    findViewById<AutoCompleteTextView>(R.id.job_date_text).text.toString(),
                    findViewById<AutoCompleteTextView>(R.id.job_type_text).text.toString(),
                    topic
                )
                startActivity(Intent(this, JobActivity::class.java))
                finish()
            }, 1000)
        }, 2000)
    }

    private fun doneButton(){
        val viewer = findViewById<View>(R.id.job_save_btn)
        var ca = true
        viewer.setOnClickListener {
            if (findViewById<AutoCompleteTextView>(R.id.job_subject_text).text.toString() == "Hinzufügen") { startActivity(Intent(this, SubjectManagement::class.java)); finish() ; return@setOnClickListener }
            if (!ca) return@setOnClickListener
            ca = false
            animation()
        }
    }
}