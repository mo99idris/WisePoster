package com.example.wiseposter

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

open class LoginAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


       checkPermission()

        var sp = getSharedPreferences("cookie", MODE_PRIVATE)
        var editor = sp.edit()


        if (sp.getString("user_id", "") != ""
                && sp.getString("pwd", "") != ""
                && sp.getString("name", "") != ""
                && sp.getString("user_type", "") != "")
                {
                    if (sp.getString("user_id", "") == "000"
                        && sp.getString("pwd", "") == "000")
                    {
                        startActivity(Intent(this, ReportACT::class.java))
                        finish()
                    }

                    else
                    {
                        AppInfo.user_id = sp.getString("user_id", "").toString()
                        AppInfo.user_type = sp.getString("user_type", "").toString()
                        AppInfo.name = sp.getString("name", "").toString()

                        startActivity(Intent(this, DashboardACT::class.java))
                        finish()
                    }

                }


       login_btn.setOnClickListener {



           var pd = ProgressDialog(this)
           pd.setMessage("Please wait..")
           pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
           pd.show()

           var url:String = AppInfo.webLocal + "php_files/login.php"

           var rq = Volley.newRequestQueue(this)
           var jar = JsonArrayRequest(Request.Method.POST, url, null, {
               response -> pd.hide()

               var pwd:String = ""
              var flag:Boolean = true

               if (login_userID_et.text.toString() != ""
                       && login_pwd_et.text.toString() != "") {
                   for (x in 0..response.length() - 1)
                   {
                       if (login_userID_et.text.toString() == response.getJSONObject(x).getString("id")
                               && login_pwd_et.text.toString() == response.getJSONObject(x).getString("password")) {

                           pwd = response.getJSONObject(x).getString("password")
                           AppInfo.user_id = response.getJSONObject(x).getString("id")

                           AppInfo.name = response.getJSONObject(x).getString("name")

                          //Toast.makeText(this, "Welcome " + AppInfo.name, Toast.LENGTH_LONG).show()

                          flag = false


                           if (response.getJSONObject(x).getString("user-type") == "admin")
                           {
                               AppInfo.user_type = "Admin"
                           }
                           else
                           {
                               AppInfo.user_type = "Student"
                           }

                           if (login_remember_ch.isChecked)
                           {
                               editor.putString("name", AppInfo.name)
                               editor.putString("user_type", AppInfo.user_type)
                               editor.putString("user_id", AppInfo.user_id)
                               editor.putString("pwd", pwd)
                               editor.commit()
                           }

                           if (AppInfo.user_id == "000"
                               && pwd == "000")
                           {
                               startActivity(Intent(this, ReportACT::class.java))
                               finish()
                               break
                           }

                           startActivity(Intent(this, DashboardACT::class.java))
                           finish()
                           break
                       }

                      else if (login_userID_et.text.toString() == response.getJSONObject(x).getString("id")
                               && login_pwd_et.text.toString() != response.getJSONObject(x).getString("password"))
                       {
                           Toast.makeText(this, "Your Password is wrong", Toast.LENGTH_LONG).show()
                           login_pwd_et.setText("")
                           flag = false
                           break
                       }

                   }
                    if (flag == true)
                    {
                        Toast.makeText(this, "Password or ID is wrong!", Toast.LENGTH_LONG).show()
                        login_pwd_et.setText("")
                        login_userID_et.setText("")
                    }

               }
               else if (login_userID_et.text.toString() != ""
                       && login_pwd_et.text.toString() == "")
               {
                   Toast.makeText(this, "Enter Password ", Toast.LENGTH_LONG).show()

               }
               else if (login_userID_et.text.toString() == ""
                       && login_pwd_et.text.toString() != "")
               {
                   Toast.makeText(this, "Enter ID ", Toast.LENGTH_LONG).show()

               }
               else
               {
                   Toast.makeText(this, "Enter ID and Password", Toast.LENGTH_LONG).show()


               }
           }, {
               error -> pd.hide()
               Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
           })

           rq.add(jar)

       }
    }

    private fun checkPermission(){

        if(Build.VERSION.SDK_INT > 22){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE), 111)

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 111)
        {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[1] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Need Permissions To Use Features App", Toast.LENGTH_LONG).show()

        }
    }



}