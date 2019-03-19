package com.example.sg772.textonimage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.View
import android.widget.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    lateinit var addImage: Button
    lateinit var image: ImageView
    lateinit var imageUri: Uri
    lateinit var addText: Button
    lateinit var mainCanvas: FrameLayout
    var xPos: Float = 0.0f
    var yPos: Float = 0.0f

    companion object {
        val PICKIMAGE = 100
        val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //init
        addImage = findViewById(R.id.addImageButton)
        image = findViewById(R.id.imageView)
        addText = findViewById(R.id.addTextButton)
        mainCanvas=findViewById(R.id.mainCanvas)
        //
        //add Image
        addImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    var perm = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(perm, PERMISSION_CODE)
                } else {

                }
            } else {

            }

            var gallery = Intent(Intent.ACTION_PICK)
            gallery.setType("image/*")
            startActivityForResult(gallery, PICKIMAGE)
        }
        //add Text
        addText.setOnClickListener {
var text=TextView(this)
            text.text="Hello"
            mainCanvas.addView(text)
            text.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action==MotionEvent.ACTION_MOVE){
                    text.x=event.x
                    text.y=event.y

                }
                    return true
                }
            })
        }

    }


    private fun pickImageFromGallery() {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICKIMAGE) {
            imageUri = data!!.data
            image.setImageURI(imageUri)
        }

    }
}
