package com.dev.cloudinary_imageuploadexample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {
    var config: HashMap<String, String> = HashMap()
    var imgpath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        config.put("cloud_name", "myCloudName")
        config.put("api_key", "636353683782376")
        config.put("api_secret", "zF5lqkUauOYaMXQ1eJPhp-keutM")
        MediaManager.init(this, config);

        save.setOnClickListener {
            ImagePicker.with(this)
                    .crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
        }

        send.setOnClickListener {
            uploadToCloudinary(imgpath.toString())
        }

    }

    fun uploadToCloudinary(filepath: String) {
        MediaManager.get().upload(filepath).unsigned("sample_app_preset").callback(object : UploadCallback {
            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                Toast.makeText(applicationContext, "Task successful", Toast.LENGTH_SHORT).show()
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                TODO("Not yet implemented")
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                TODO("Not yet implemented")
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Toast.makeText(applicationContext, "Task Not successful", Toast.LENGTH_SHORT).show()
            }

            override fun onStart(requestId: String?) {
                save.text = "start"
                Toast.makeText(applicationContext, "Start", Toast.LENGTH_SHORT).show()
            }
        }).dispatch()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data

            imagev.setImageURI(fileUri)

            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath: String = ImagePicker.getFilePath(data)!!
            imgpath = filePath
           uploadToCloudinary(filePath)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


}