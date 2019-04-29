package com.example.sg772.textonimage

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.example.sg772.textonimage.Adapter.ViewPagerAdapter
import com.example.sg772.textonimage.Interfaces.EditImageFragmentListener
import com.example.sg772.textonimage.Interfaces.FiltersFragmentListener
import com.example.sg772.textonimage.Utils.BitmapUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import android.Manifest.permission
import android.net.Uri
import android.support.design.widget.Snackbar
import android.text.TextUtils
import com.karumi.dexter.listener.*

class MainActivity : AppCompatActivity(), FiltersFragmentListener, EditImageFragmentListener {


    lateinit var addImage: Button
    lateinit var image_preview: ImageView
    lateinit var viewPager: ViewPager
    lateinit var tableLayout: android.support.design.widget.TabLayout
    lateinit var coordinatorLayout: android.support.constraint.ConstraintLayout
    lateinit var original_filter_bitmap: Bitmap
    lateinit var filtered_bitmap: Bitmap
    lateinit var final_bitmap: Bitmap
    lateinit var editImageFragment: EditImageFragment
    lateinit var imageFiltersFragment: ImageFiltersFragment

    var brightnessFinal = 0
    var constrantFinal: Float = 1.0f
    var saturationFinal: Float = 1.0f
//Load native image filters


    companion object {
        val PICKIMAGE = 1000
        open val pictureName: String = "flash.jpg"
        var native_libs = System.loadLibrary("NativeImageProcessor")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Instagram filters")
        //View
        image_preview = findViewById(R.id.imagepreview)
        viewPager = findViewById(R.id.viewpager)
        tableLayout = findViewById(R.id.tabs)
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        loadImage()
        setupViewPager(viewPager)
        tableLayout.setupWithViewPager(viewPager)

    }

    private fun loadImage() {
        Log.d("pictureName", pictureName)
        original_filter_bitmap = BitmapUtils.getBitmapFromAsests(this, pictureName, 300, 300)
        filtered_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
        final_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.setImageBitmap(original_filter_bitmap)
    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        var myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.setImageBitmap(myFilter.processFilter(final_bitmap.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        var myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.setImageBitmap(myFilter.processFilter(final_bitmap.copy(Bitmap.Config.ARGB_8888, true)))

    }

    override fun onConstrantChanged(constrant: Float) {
        constrantFinal = constrant
        var myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(constrant))
        image_preview.setImageBitmap(myFilter.processFilter(final_bitmap.copy(Bitmap.Config.ARGB_8888, true)))

    }

    override fun onEditStarted() {

    }

    override fun onEditCompleted() {
        var bitmap = final_bitmap.copy(Bitmap.Config.ARGB_8888, true)
        var myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(ContrastSubFilter(constrantFinal))
        final_bitmap = myFilter.processFilter(bitmap)

    }

    override fun onFilterSelected(filter: Filter) {
        resetControlls()
        filtered_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.setImageBitmap(filter.processFilter(filtered_bitmap))
        final_bitmap = filtered_bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun resetControlls() {
        if (editImageFragment != null) {
            editImageFragment.resetControlls()
        }
        brightnessFinal = 0
        saturationFinal = 1.0f
        constrantFinal = 1.0f
    }


    private fun setupViewPager(viewPager: ViewPager?) {
        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        imageFiltersFragment = ImageFiltersFragment()
        imageFiltersFragment.listener = this
        editImageFragment = EditImageFragment()
        editImageFragment.listener = this
        viewPagerAdapter.addFragment(imageFiltersFragment, "filters")
        viewPagerAdapter.addFragment(editImageFragment, "edit")
        viewPager?.adapter = viewPagerAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_open -> {
                openOmageFromGallery()
            }
            R.id.action_save -> {
                saveImageToGallery()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveImageToGallery() {
        Dexter.withActivity(this)
            .withPermissions(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    val stringPath = BitmapUtils.insertInGallery(
                        contentResolver,
                        final_bitmap,
                        "${System.currentTimeMillis()}.jpg",
                        null
                    )
                    if (!TextUtils.isEmpty(stringPath)) {
                        var snackbar =
                            Snackbar.make(coordinatorLayout, "Image saved to gallery! ", Snackbar.LENGTH_LONG)
                                .setAction("OPEN", object : View.OnClickListener {
                                    override fun onClick(v: View?) {
                                        openImage(stringPath)
                                    }
                                })
                                .show()
                    } else {
                        var snackbar =
                            Snackbar.make(coordinatorLayout, "Failed to save image! ", Snackbar.LENGTH_LONG)
                                .show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            })
            .check()
    }

    private fun openImage(stringPath: String?) {
        var intent = Intent()
        intent.setAction(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(stringPath), "image/*")
        startActivity(intent)

    }

    private fun openOmageFromGallery() {
        Dexter.withActivity(this)
            .withPermissions(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    var intent = Intent(Intent.ACTION_PICK)
                    intent.setType("image/*")
                    startActivityForResult(intent, PICKIMAGE)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            })
            .check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICKIMAGE && resultCode == RESULT_OK && data != null) {
            data?.data?.let {
                val bitmap = BitmapUtils.getBitmapFromGallery(this, it, 800, 800)

                original_filter_bitmap.recycle()
                final_bitmap.recycle()
                filtered_bitmap.recycle()
                original_filter_bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                final_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filtered_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
                image_preview.setImageBitmap(original_filter_bitmap)
                bitmap.recycle()
                imageFiltersFragment.displayThumbNail(original_filter_bitmap)

            }
        }
    }
}
