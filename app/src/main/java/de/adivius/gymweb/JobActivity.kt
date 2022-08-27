package de.adivius.gymweb

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import java.util.*

class JobActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job)
        supportActionBar!!.title = "Tests & Arbeiten"


        drawer()
        list()
        listtheSubs(this)

        findViewById<FloatingActionButton>(R.id.job_add_btn).setOnClickListener {
            startActivity(Intent(this, AddJob::class.java))
            finish()
        }



    }
    fun drawer(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_job)
        val navView: NavigationView = findViewById(R.id.navView_job)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar    ?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            var i = MainActivity::class.java

            startActivity(Intent(this, i))

            when (it.itemId){

                R.id.nav_menu_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_menu_subject -> startActivity(Intent(this, SubjectManagement::class.java))
                R.id.nav_menu_time -> startActivity(Intent(this, TimeTable::class.java))
                R.id.nav_menu_work -> startActivity(Intent(this, JobActivity::class.java))
                R.id.nav_menu_settings -> startActivity(Intent(this, Settings::class.java))
                R.id.nav_menu_share -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            true
        }
    }

    private fun list() {
        val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
        val gson = Gson()
        var json = sharedPref.getString("jobs", null)
        val type = object : TypeToken<ArrayList<Job>>() {}.type
        JobArray = gson.fromJson<ArrayList<Job>>(json, type) ?: return

        var job_rec = findViewById<RecyclerView>(R.id.job_rec)
        job_rec.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        var adapter = GroupAdapter<GroupieViewHolder>()
        job_rec.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            var pos = adapter.getAdapterPosition(item)
            var jobitem = item as JobItem
            deleteDialog(jobitem.jobitem.day, pos)
        }


        for (i in JobArray) {
            adapter.add(JobItem(i))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    fun deleteDialog(itemname: String, itempos: Int){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Löschen")
        builder.setPositiveButton("Löschen") { dialog, which ->

            JobArray.removeAt(itempos)
            val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            var json = gson.toJson(JobArray)
            editor.putString("jobs", json)
            editor.apply()
            finish();startActivity(Intent(this, JobActivity::class.java))

        }
        builder.setNegativeButton("Abbrechen") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }
}