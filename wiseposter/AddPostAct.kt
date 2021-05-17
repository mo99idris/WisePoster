package com.example.wiseposter

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.edit_toolbar.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap


class AddPostAct : AppCompatActivity() {
    var encodeImageString:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        text_tweet_et.setText(AppInfo.tweet_update)

        if(AppInfo.photo_update != ""){

            var ph:String = AppInfo.photo_update.replace(" ","%20")
            Picasso.get().load(AppInfo.webLocal + "php_files/uploads/" + ph).into(insert_img)
            hint_img_tv.text = ""
            remove_img.visibility = View.VISIBLE

        }

        else
        {
            remove_img.visibility = View.INVISIBLE
            hint_img_tv.text = "insert Image"
            insert_img.setImageResource(R.drawable.gradient_bg)
        }



        arrow_back_toolbar.setOnClickListener {
            onBackPressed()
        }


        insert_img.setOnClickListener {
            checkPermission()
            var flag:Boolean = true


        }


            upload_btn.setOnClickListener {

                if (text_tweet_et.text.toString() != "")
                {
                   uploadPost()
                }
                else
                {
                    Toast.makeText(this, "empty tweet !!", Toast.LENGTH_LONG).show()
                }
            }

        remove_img.setOnClickListener {
            hint_img_tv.text = "insert Image"
            insert_img.setImageResource(R.drawable.gradient_bg)
            remove_img.visibility = View.INVISIBLE
            encodeImageString = ""
            AppInfo.photo_update = ""
        }

    }



    private fun checkPermission(){

        if(Build.VERSION.SDK_INT > 22){
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                loadImage()


            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE), 222)
        }
        else
            loadImage()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 222){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED ){
                loadImage()

            }
            else{
                Toast.makeText(this, "Can't uploud image", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadImage(){
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)

    }

    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null && resultCode == RESULT_OK) {

            var result = CropImage.getActivityResult(data)
            var resultUri = result.getUri()
            insert_img.setImageURI(resultUri)
            hint_img_tv.text = ""
            remove_img.visibility = View.VISIBLE

            var inputStream = contentResolver.openInputStream(resultUri)
            var bitmap = BitmapFactory.decodeStream(inputStream)

            encodeBitmapImage(bitmap)

        }
        else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            var result = CropImage.getActivityResult(data);
            var error: Exception = result.getError();
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun encodeBitmapImage(bitmap: Bitmap) {


        var byteArrayOutPutStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutPutStream)
        var byteOfImage = byteArrayOutPutStream.toByteArray()
        encodeImageString = Base64.encodeToString(byteOfImage, Base64.DEFAULT)

    }

    private fun uploadPost(){

        var currentTime = Calendar.getInstance().time
        var formatedDate:String = DateFormat.getInstance().format(currentTime)

        var pd = ProgressDialog(this)
        pd.setMessage("Please wait..")
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        pd.show()

        var url:String = AppInfo.webLocal + "php_files/uplaodPost.php"

        var rq = Volley.newRequestQueue(this)
        var sr = object :StringRequest(Request.Method.POST, url, {
                response -> pd.hide()

            finish()
        }, {
                error -> pd.hide()
            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()

                if(AppInfo.flagEdit == false)
                {
                    map.put("admin_id", AppInfo.user_id)
                    map.put("p_text", text_tweet_et.text.toString())
                    map.put("p_date", formatedDate)
                    map.put("image", encodeImageString)
                }



                else
                {
                    map.put("p_text", text_tweet_et.text.toString())
                    map.put("post_id", AppInfo.post_id.toString())
                    map.put("update_ph", AppInfo.photo_update)
                    map.put("image", encodeImageString)
                }


                return map
            }
        }
        rq.add(sr)
    }

}