package de.adivius.gymweb

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val list: ArrayList<Homework> = downloadItems(context)
        var items = "\n"; var count = 0
        for (item in list){
            items += "- ${item.subject}\n\n"
            count += 1
        }
        items = items.dropLast(2)
        val builder = NotificationCompat.Builder(context!!, "HomeWorkChannel")
            .setSmallIcon(R.drawable.ic_content)
            .setContentTitle("Es stehen $count Hausaufgaben aus")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .setBigContentTitle("Ausstehende Hausaufgaben:")
                .bigText(items)
            )
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1017, builder.build())
    }
    private fun downloadItems(context: Context?): ArrayList<Homework> {
        val sharedPref = context!!.getSharedPreferences("Devs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("tasks", null)
        val type = object : TypeToken<ArrayList<Homework>>() {}.type
        return gson.fromJson(json, type)
    }
}