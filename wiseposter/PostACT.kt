package com.example.wiseposter

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.wiseposter.AppInfo.Companion.user_type
import kotlinx.android.synthetic.main.activity_post_a_c_t.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostACT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_a_c_t)


        var listModel = ArrayList<PostData>()

        if(user_type == "Student")
            fab.visibility = View.INVISIBLE

        else
            fab.visibility = View.VISIBLE


        fab.setOnClickListener {
            startActivity(Intent(this, AddPostAct::class.java))
            AppInfo.tweet_update = ""
            AppInfo.photo_update = ""
            AppInfo.flagEdit = false

        }



        var pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()


        var url = AppInfo.webLocal + "php_files/retrievePosts.php"
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.POST, url, null, {
                response -> pd.hide()

            for (x in 0..response.length()-1)
            {


                listModel.add(PostData(response.getJSONObject(x).getString("profile_img"),
                    response.getJSONObject(x).getString("name"),
                    response.getJSONObject(x).getString("p_date"),
                    response.getJSONObject(x).getString("p_text"),
                    response.getJSONObject(x).getString("image"),
                    response.getJSONObject(x).getInt("post_id")))

            }

        }, {
                error -> pd.hide()
            Toast.makeText(this, error.message.toString(), Toast.LENGTH_LONG).show()
        })

        rq.add(jar)



        var adp = PostAdp(this, listModel)
        post_rv.adapter = adp
        post_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

      //  post_rv.scrollToPosition(listModel.lastIndex)

        post_swp.setOnRefreshListener {
            startActivity(Intent(this, PostACT::class.java))
            finish()

        }

    }

}