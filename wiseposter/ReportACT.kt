package com.example.wiseposter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_post_a_c_t.*
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.report_toolbar.*

class ReportACT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        report_swp.setOnRefreshListener {
            startActivity(Intent(this, ReportACT::class.java))
            finish()
        }

        logout_toolbar.setOnClickListener {
            var sp = getSharedPreferences("cookie", MODE_PRIVATE)
            sp.edit().clear().commit()
            startActivity(Intent(this, LoginAct::class.java))
            finish()
        }

        var listModel = ArrayList<ReportData>()

        var pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()


        var url = AppInfo.webLocal + "php_files/SelectReport.php"
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.POST, url, null, {
                response -> pd.hide()

            for (x in 0 until response.length())
            {

                listModel.add(ReportData(response.getJSONObject(x).getString("name"),
                    response.getJSONObject(x).getString("Ruser_id"),
                    response.getJSONObject(x).getString("report_text")))

            }

        }, {
                error -> pd.hide()
            Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
        })

        rq.add(jar)



        var adp = ReportAdp(this, listModel)
        report_rv.adapter = adp
        report_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        report_rv.scrollToPosition(listModel.size -1)
    }
}