package de.adivius.gymweb

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
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


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = "Hausaufgaben"

        downloadTasks()
        putTasks()
        drawer()

        findViewById<FloatingActionButton>(R.id.main_add).setOnClickListener {
            val intent = Intent(this, HW_Input::class.java)
            startActivity(intent)
        }

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (HomeArray.size == 0) helptoast("Füge Hausaufgaben mit dem Plus hinzu")
    }


    fun downloadTasks() {
        val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
        val gson = Gson()
        var json = sharedPref.getString("tasks", null)
        Log.d("Devs", json ?: "NotFound")
        val type = object : TypeToken<ArrayList<Homework>>() {}.type
        HomeArray = gson.fromJson<ArrayList<Homework>>(json, type) ?: return
    }

    fun putTasks() {

        var main_rec = findViewById<RecyclerView>(R.id.main_rec)
        main_rec.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        var adapter = GroupAdapter<GroupieViewHolder>()
        main_rec.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            var pos = adapter.getAdapterPosition(item)
            var wsitem = item as HomeListItem
            val intent = Intent(view.context, Description::class.java)
            intent.putExtra("subject", wsitem.workitem.subject)
            intent.putExtra("day", wsitem.workitem.day)
            intent.putExtra("task", wsitem.workitem.task)
            intent.putExtra("pos", pos.toString())
            startActivity(intent)
        }


        for (i in HomeArray) {
            adapter.add(HomeListItem(Homework(i.subject, i.task, i.day)))
        }
    }
    fun drawer(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_main)
        val navView: NavigationView = findViewById(R.id.navView_main)

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
                R.id.nav_menu_work-> startActivity(Intent(this, JobActivity::class.java))
                R.id.nav_menu_settings -> startActivity(Intent(this, Settings::class.java))
                R.id.nav_menu_rate -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))


                R.id.nav_menu_share -> {
                    val shareBody = "https://play.google.com/store/apps/details?id=$packageName"
                    val shareintent = Intent(Intent.ACTION_SEND)
                    shareintent.type = "text/plain"
                    shareintent.putExtra(Intent.EXTRA_TEXT, shareBody)
                    val chooser = Intent.createChooser(shareintent, "Teilen über...")
                    startActivity(shareintent)
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }


}