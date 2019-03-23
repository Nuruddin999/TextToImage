package com.example.sg772.textonimage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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

        //


    }
}
