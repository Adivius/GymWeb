package de.adivius.gymweb

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout

class ProgressButton(ct: Context, view: View) {

    val cardView = view.findViewById<CardView>(R.id.save_btn_CardView)
    val constraintLayout = view.findViewById<ConstraintLayout>(R.id.save_btn_ConstraintLayout)
    val progressBar = view.findViewById<ProgressBar>(R.id.save_btn_ProgressBar)
    val text = view.findViewById<TextView>(R.id.save_btn_Text)


    fun buttonActivated() {
        progressBar.visibility = View.VISIBLE
        text.text = "SPEICHERN..."
    }

    fun buttonFinished() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.lime))
        progressBar.visibility = View.GONE
        text.text = "GESPEICHERT"
    }

}