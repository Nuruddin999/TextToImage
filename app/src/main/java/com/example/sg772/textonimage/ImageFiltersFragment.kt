package com.example.sg772.textonimage

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.sg772.textonimage.Adapter.ThumbnailAdapter
import com.example.sg772.textonimage.Interfaces.FiltersFragmentListener
import com.example.sg772.textonimage.Utils.BitmapUtils
import com.example.sg772.textonimage.Utils.SpacesItemDecoration
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.utils.ThumbnailItem
import com.zomato.photofilters.utils.ThumbnailsManager


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ImageFiltersFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ImageFiltersFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ImageFiltersFragment : BottomSheetDialogFragment(), FiltersFragmentListener {


    // TODO: Rename and change types of parameters
    internal var listener: FiltersFragmentListener? = null
    lateinit var recyclerView: RecyclerView
    internal var thumbnailList: MutableList<ThumbnailItem>?=null
    internal lateinit var thumbnailAdapter: ThumbnailAdapter

    companion object {
        internal var instance: ImageFiltersFragment? = null
        fun getInstance(): ImageFiltersFragment {
            if (instance == null) {
                instance = ImageFiltersFragment()
            }
            return instance!!
        }

    }

    override fun onStart() {
        super.onStart()
        var dialog: Dialog = dialog
        if (dialog != null) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var itemView: View = inflater.inflate(R.layout.fragment_image_filters, container, false)
        thumbnailList = ArrayList()
        thumbnailAdapter = ThumbnailAdapter(thumbnailList!!, this, activity)
        recyclerView = itemView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        var space: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics)
        recyclerView.addItemDecoration(SpacesItemDecoration(space.toInt()))
        recyclerView.adapter = thumbnailAdapter
        displayThumbNail(null)
        return itemView
    }

    open fun displayThumbNail(bitmap: Bitmap?) {
        var runnable = Runnable {

            var thumbImg: Bitmap?
            if (bitmap == null)
                thumbImg = BitmapUtils.getBitmapFromAsests(activity, MainActivity.pictureName, 100, 100)
            else
                thumbImg = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

            if (thumbImg == null)
                return@Runnable

            ThumbnailsManager.clearThumbs()
            thumbnailList?.clear()
            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImg
            thumbnailItem.filterName = "Normal"
            ThumbnailsManager.addThumb(thumbnailItem)
            var filters = FilterPack.getFilterPack(activity!!) as MutableList
            for (f in filters) {
                val item = ThumbnailItem()
                item.image = thumbImg
                item.filter = f
                item.filterName = f.name
                ThumbnailsManager.addThumb(item)
                Log.d("filterpack", f.name)
            }
            thumbnailList!!.addAll(ThumbnailsManager.processThumbs(activity))
            activity!!.runOnUiThread {

                thumbnailAdapter.notifyDataSetChanged()

            }
        }

        Thread(runnable).start()
    }

    override fun onFilterSelected(filter: Filter) {
        if (listener != null) {
            listener!!.onFilterSelected(filter)
        }
    }
}
