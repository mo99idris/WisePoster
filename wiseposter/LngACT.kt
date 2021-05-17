package com.example.wiseposter

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lng_a_c_t.*
import java.util.*

class LngACT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lng_a_c_t)

        tv_ar.setOnClickListener {
            changeLng("ar")

        }

        tv_en.setOnClickListener {
            changeLng("en")

        }

    }
    fun changeLng(lng: String){

        var loc = Locale(lng)
        Locale.setDefault(loc)
        var conf = Configuration()
        conf.locale = loc

        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)

        val i = Intent(this, DashboardACT::class.java)
        startActivity(i)
        finishAffinity()

    }
}