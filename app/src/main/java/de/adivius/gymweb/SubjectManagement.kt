package de.adivius.gymweb

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class SubjectManagement : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_management)
        supportActionBar!!.title = "Fächer"

        list()
        drawer()



        findViewById<Button>(R.id.sub_add).setOnClickListener {
            addDialog()
        }
    }

    private fun list() {
        val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
        val gson = Gson()
        var json = sharedPref.getString("subjects", null)
        val type = object : TypeToken<ArrayList<Subject>>() {}.type
        Subjects = gson.fromJson<ArrayList<Subject>>(json, type) ?: return

        var sub_rec = findViewById<RecyclerView>(R.id.sub_rec)
        sub_rec.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        var adapter = GroupAdapter<GroupieViewHolder>()
        sub_rec.adapter = adapter
        adapter.setOnItemClickListener { item, view ->
            var pos = adapter.getAdapterPosition(item)
            var subitem = item as SubjectItem
            deleteDialog(subitem.subitem.name, pos)
        }


        for (i in Subjects) {
            adapter.add(SubjectItem(i))
        }
    }

    fun deleteDialog(itemname: String, itempos: Int){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("$itemname Löschen")
        builder.setPositiveButton("Löschen") { dialog, which ->

            Subjects.removeAt(itempos)
            val sharedPref = getSharedPreferences("Devs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            var json = gson.toJson(Subjects)
            editor.putString("subjects", json)
            editor.apply()
            finish();startActivity(Intent(this, SubjectManagement::class.java))

        }
        builder.setNegativeButton("Abbrechen") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    fun addDialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Fach hinzufügen")

        val text = EditText(this)
        text.hint = "Fach"
        builder.setView(text)
        builder.setPositiveButton("Hinzufügen") { dialog, which ->
            addSubject(this, text.text.toString())
            finish();startActivity(Intent(this, SubjectManagement::class.java))
        }
        builder.setNegativeButton("Abbrechen") { dialog, which ->
            dialog.cancel()
        }
        builder.show()
    }

    fun drawer(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_sub)
        val navView: NavigationView = findViewById(R.id.navView_sub)

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
                R.id.nav_menu_share -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}