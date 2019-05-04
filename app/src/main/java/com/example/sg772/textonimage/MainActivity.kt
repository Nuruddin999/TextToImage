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
import android.graphics.Typeface
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v7.widget.CardView
import android.text.TextUtils
import com.example.sg772.textonimage.Interfaces.BrushListener
import com.example.sg772.textonimage.Interfaces.textOnImageListener
import com.karumi.dexter.listener.*
import com.zomato.photofilters.FilterPack
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import java.io.ByteArrayOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity(), FiltersFragmentListener, EditImageFragmentListener, BrushListener,
    textOnImageListener {


  internal  lateinit var filters_menu: CardView
    internal   lateinit var edit_menu: CardView
    internal   lateinit var brush_menu: CardView
    internal  lateinit var imageadd_menu: CardView
    internal  lateinit var textadd_menu: CardView
    internal  lateinit var image_preview: PhotoEditorView
    internal    lateinit var photoEditor: PhotoEditor
    internal  lateinit var coordinatorLayout: android.support.constraint.ConstraintLayout
    open  lateinit var original_filter_bitmap: Bitmap
    internal  lateinit var filtered_bitmap: Bitmap
    internal    lateinit var final_bitmap: Bitmap
  lateinit  var editImageFragment: EditImageFragment
  lateinit var imageFiltersFragment: ImageFiltersFragment

   internal var brightnessFinal = 0
    internal  var constrantFinal: Float = 1.0f
    internal   var saturationFinal: Float = 1.0f
//Load native image filters


    companion object {
        val INSERTIMAGE = 1000
        val PICKIMAGE = 1001
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
        photoEditor = PhotoEditor.Builder(this, image_preview).setPinchTextScalable(true).build()
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        filters_menu = findViewById(R.id.filters_menu)
        edit_menu = findViewById(R.id.edit_menu)
        brush_menu = findViewById(R.id.brush_menu)
        textadd_menu = findViewById(R.id.addText_menu)
        imageadd_menu = findViewById(R.id.adImage_menu)
        loadImage()
        imageFiltersFragment=ImageFiltersFragment.getInstance()

        filters_menu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
if(imageFiltersFragment!=null){
    var  byteArrayOutputStream=ByteArrayOutputStream()
    original_filter_bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
    var byteArray=byteArrayOutputStream.toByteArray()
    var bundle=Bundle()
    bundle.putByteArray("image",byteArray)
    imageFiltersFragment.arguments=bundle
                imageFiltersFragment!!.listener = this@MainActivity

              /*  var fragmentTransaction=supportFragmentManager.beginTransaction().replace(R.id.content_area,imageFiltersFragment).commit()*/
                imageFiltersFragment.show(supportFragmentManager, imageFiltersFragment!!.tag)}

            }
        })
        edit_menu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var editImageFragment = EditImageFragment.newInstance()
                editImageFragment.listener = this@MainActivity
                editImageFragment.show(supportFragmentManager, editImageFragment.tag)
            }
        })
        brush_menu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                photoEditor.setBrushDrawingMode(true)
                var brushFragment = BrushFragment.newInstance()
                brushFragment.listener = this@MainActivity
                brushFragment.show(supportFragmentManager, brushFragment.tag)
            }
        })
        textadd_menu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var textFragment = TextFragment.newInstance()
                textFragment.addTextLIstener = this@MainActivity
                textFragment.show(supportFragmentManager, textFragment.tag)
            }
        })
        imageadd_menu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
               addImage()
            }
        })




    }

    private fun addImage() {
        Dexter.withActivity(this)
            .withPermissions(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    var intent = Intent(Intent.ACTION_PICK)
                    intent.setType("image/*")
                    startActivityForResult(intent, INSERTIMAGE)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }
            })
            .check()
    }

    private fun loadImage() {
        Log.d("pictureName", pictureName)
        original_filter_bitmap = BitmapUtils.getBitmapFromAsests(this, pictureName, 300, 300)
        filtered_bitmap = original_filter_bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
        final_bitmap = original_filter_bitmap!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.source.setImageBitmap(original_filter_bitmap)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        imageFiltersFragment = ImageFiltersFragment()
        imageFiltersFragment!!.listener = this
        editImageFragment = EditImageFragment()
        editImageFragment!!.listener = this
        viewPagerAdapter.addFragment(imageFiltersFragment!!, "filters")
        viewPagerAdapter.addFragment(editImageFragment!!, "edit")
        viewPager?.adapter = viewPagerAdapter
    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        var myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.source.setImageBitmap(myFilter.processFilter(final_bitmap.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        var myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.source.setImageBitmap(myFilter.processFilter(final_bitmap.copy(Bitmap.Config.ARGB_8888, true)))

    }

    override fun onConstrantChanged(constrant: Float) {
        constrantFinal = constrant
        var myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(constrant))
        image_preview.source.setImageBitmap(myFilter.processFilter(final_bitmap.copy(Bitmap.Config.ARGB_8888, true)))

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
        image_preview.source.setImageBitmap(filter.processFilter(filtered_bitmap))
        final_bitmap = filtered_bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun resetControlls() {
        if (editImageFragment != null)
            editImageFragment!!.resetControlls()
        brightnessFinal = 0
        saturationFinal = 1.0f
        constrantFinal = 1.0f


    }

    //Brush
    override fun brushSizeChangeListener(size: Float) {
        photoEditor.brushSize = size
    }

    override fun brushOpacityChangeListener(opacity: Int) {
        photoEditor.setOpacity(opacity)
    }

    override fun brushColorChangeListener(color: Int) {
        photoEditor.brushColor = color
    }

    override fun brushStateChangeListener(isEraser: Boolean) {
        if (isEraser)
            photoEditor.brushEraser()
        else
            photoEditor.setBrushDrawingMode(true)
    }

    //Brush-end
    //Add Text
    override fun adTextListener(typeface: Typeface,text: String, color: Int) {
        photoEditor.addText(typeface,text,color)
    }

    //Add Text-end
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
                    photoEditor.saveAsBitmap(object : OnSaveBitmap {
                        override fun onFailure(e: Exception?) {
                        }

                        override fun onBitmapReady(saveBitmap: Bitmap?) {
                            image_preview.source.setImageBitmap(saveBitmap)
                            val stringPath = BitmapUtils.insertInGallery(
                                contentResolver,
                                saveBitmap,
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
                    })
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

        if (resultCode == RESULT_OK && data!=null)  {
            if (requestCode == PICKIMAGE) {
                data?.data?.let {
                    val bitmap = BitmapUtils.getBitmapFromGallery(this, it, 800, 800)

                    original_filter_bitmap.recycle()
                    final_bitmap.recycle()
                    filtered_bitmap.recycle()
                    original_filter_bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    final_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    filtered_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    image_preview.source.setImageBitmap(original_filter_bitmap)
                 //   imageFiltersFragment.displayThumbNail(original_filter_bitmap)



                original_filter_bitmap.recycle()
                final_bitmap.recycle()
                filtered_bitmap.recycle()
                original_filter_bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                final_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filtered_bitmap = original_filter_bitmap.copy(Bitmap.Config.ARGB_8888, true)
                image_preview.source.setImageBitmap(original_filter_bitmap)
                bitmap.recycle()
                imageFiltersFragment.displayThumbNail(original_filter_bitmap)


                }

            } else if
                           (requestCode == INSERTIMAGE)
            {
                data?.data?.let {
                    var bitmap = BitmapUtils.getBitmapFromGallery(this, it, 300, 300)
                    photoEditor.addImage(bitmap)
                }
            }

        }
    }
}
