package com.example.wiseposter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.android.synthetic.main.activity_dashboard_a_c_t.*

class DashboardACT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_a_c_t)

        dashboard_posting.setOnClickListener {
            startActivity(Intent(this, PostACT::class.java))
        }

        dashboard_language.setOnClickListener {
            startActivity(Intent(this, LngACT::class.java))
        }

        dashboard_profile.setOnClickListener {
            startActivity(Intent(this, ProfileAct::class.java))
        }


        dashboard_logout.setOnClickListener {

            var sp = getSharedPreferences("cookie", MODE_PRIVATE)
            sp.edit().clear().commit()
            startActivity(Intent(this, LoginAct::class.java))
            finish()

        }

    }
}