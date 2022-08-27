package de.adivius.gymweb

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import android.app.PendingIntent
import android.app.AlarmManager
import android.util.Log
import java.lang.Exception
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate


class Settings : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        createNotificationChannel()
        drawer()
        supportActionBar!!.title = "Einstellungen"
        val Button: Button = findViewById(R.id.settings_change)
        val Switch: Switch = findViewById(R.id.settings_switch)
        Switch.isChecked = getShar(this)
        Switch.isEnabled = Button.text != "Zeit wählen" || getShar(this)

        Button.setOnClickListener {
            showTimePicker(this, Button)
            cancelAll(this)
            Switch.isChecked = false
        }

        Switch.setOnCheckedChangeListener { _, checked ->
            if (checked){
                setAlarm()
            }
            if (!checked){
                cancelAll(this)
                Switch.isEnabled = Button.text != "Zeit wählen"
            }
            setShar(this)
        }


    }

    private fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name: CharSequence = "HomeWork Channel"
            val description = "Channel for Homeworks"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("HomeWorkChannel", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            try {
                notificationManager.createNotificationChannel(channel)
            }catch (e: Exception){
                Log.d("Nof", "There Was an Error: $e")
            }finally {
                Log.d("Nof", "Nof_channel was created")
            }
        }
    }

    private fun showTimePicker(context: Context, Button: Button){


        val c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)

            Button.text = SimpleDateFormat("HH:mm").format(c.time)
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = hour
            calendar[Calendar.MINUTE] = minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            findViewById<Switch>(R.id.settings_switch).isEnabled = Button.text != "Zeit wählen"
            Log.d("Nof", "Time was set")
        }
        TimePickerDialog(context, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()


    }

    private fun setAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        try {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        } catch (e: Exception){
            Log.d("Nof", "There Was an Error: $e")
        }finally {
            Log.d("Nof", "Nof was set")
        }
    }

    private fun cancelAll(context: Context){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val updateServiceIntent = Intent(context, AlarmReceiver::class.java)
        val pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0)
        try {
            alarmManager.cancel(pendingUpdateIntent)
        } catch (e: Exception) {
            Log.e("Nof", "AlarmManager update was not canceled. $e")
        } finally {
            Log.d("Nof", "Nof are canceled")
        }
    }

    private fun setShar(context: Context) {
        val Switch: Switch = findViewById(R.id.settings_switch)
        val sharedPref = context.getSharedPreferences("Devs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean ("not_switch", Switch.isChecked)
        editor.apply()
    }

    private fun getShar(context: Context):Boolean {
        val sharedPref = context.getSharedPreferences("Devs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("not_switch", false)
    }

    private fun drawer(){
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout_settings)
        val navView: NavigationView = findViewById(R.id.navView_settings)

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