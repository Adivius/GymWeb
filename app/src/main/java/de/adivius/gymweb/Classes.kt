package de.adivius.gymweb

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.view.Gravity
import android.widget.AutoCompleteTextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.helptoast(text: String) {
    var Toasty = Toast.makeText(this, text, Toast.LENGTH_SHORT)
    Toasty.setGravity(Gravity.CENTER, 0, 0)
    Toasty.show()
}

fun days(dayend: String): String {
    val c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))

    val startyear = c.get(Calendar.YEAR)
    var startmonth = if ((c.get(Calendar.MONTH) + 1).toString().length == 1) {
        "0${(c.get(Calendar.MONTH) + 1)}"
    } else {
        (c.get(Calendar.MONTH) + 1).toString()
    }
    var startday = if (c.get(Calendar.DAY_OF_MONTH).toString().length == 1) {
        "0${c.get(Calendar.DAY_OF_MONTH)}"
    } else {
        c.get(Calendar.DAY_OF_MONTH).toString()
    }
    val endstring = dayend.split(".")
    val endyear = endstring[2]
    val endmonth = endstring[1]
    val endday = endstring[0]

    val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.GERMAN)
    val startDate = sdf.parse("$startmonth/$startday/$startyear")
    val endDate = sdf.parse("$endmonth/$endday/$endyear")

    val diffInMillies = abs(endDate.time - startDate.time)
    val between: Int = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS).toInt()

    var set: String

    if (between == 1 && startday.toInt() - endday.toInt() <0) set = "Muss in einem Tag erledigt werden"
    else if (between == 0) set = "Muss heute erledigt sein"
    else if (between >0 && startday.toInt() - endday.toInt() >0 && startmonth.toInt() - endmonth.toInt() >0 || startday.toInt() - endday.toInt() >0 && startmonth.toInt() - endmonth.toInt() >0
        || startday.toInt() - endday.toInt() >0 && startmonth.toInt() - endmonth.toInt() == 0 || startday.toInt() - endday.toInt() == 0 && startmonth.toInt() - endmonth.toInt() >0
        || startyear - endyear.toInt() >0 ) set = "Ist überfallig"
    else set = "Muss in $between Tagen erledigt werden"

    return set
}

fun today(): String {
    val c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))

    val startyear = c.get(Calendar.YEAR)
    var startmonth = if ((c.get(Calendar.MONTH) + 1).toString().length == 1) {
        "0${(c.get(Calendar.MONTH) + 1)}"
    } else {
        (c.get(Calendar.MONTH) + 1).toString()
    }
    var startday = if (c.get(Calendar.DAY_OF_MONTH).toString().length == 1) {
        "0${c.get(Calendar.DAY_OF_MONTH)}"
    } else {
        c.get(Calendar.DAY_OF_MONTH).toString()
    }

    return "$startday.$startmonth.$startyear"
}

@SuppressLint("SetTextI18n")
fun selectDate(context: Context, autoCompleteTextView: AutoCompleteTextView){

    DatePickerDialog(context, { view, mYear, mMonth, mDay ->
        //day
        val nDay = if (mDay.toString().length == 1)  "0$mDay" else mDay.toString()
        //month
        val valM = mMonth + 1
        var nMonth: String = if (valM.toString().length == 1) "0$valM" else valM.toString()
        //finnish
        autoCompleteTextView.setText("$nDay.$nMonth.$mYear")
    }, tYear().toInt(), tMonth().toInt()-1, tDay().toInt()).show()

}

fun tDay(): String{
    val c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"))
    return if (c.get(Calendar.DAY_OF_MONTH).toString().length == 1) "0${c.get(Calendar.DAY_OF_MONTH)}" else c.get(Calendar.DAY_OF_MONTH).toString()
}

fun tMonth(): String{
    val valM = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")) .get(Calendar.MONTH) + 1
    return if(valM.toString().length == 1) "0$valM" else valM.toString()
}

fun tYear(): String{
    return Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).get(Calendar.YEAR).toString()
}




