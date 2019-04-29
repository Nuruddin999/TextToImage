package com.example.sg772.textonimage

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class ImageFiltersFragment : Fragment(), FiltersFragmentListener {


    // TODO: Rename and change types of parameters
    lateinit var listener: FiltersFragmentListener
    lateinit var recyclerView: RecyclerView
 var thumbnailList: ArrayList<ThumbnailItem>?=null
    lateinit var thumbnailAdapter: ThumbnailAdapter


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
        var runnable = object : Runnable {
            override fun run() {
                var thumbImg: Bitmap?
                if (bitmap == null)
                    thumbImg = BitmapUtils.getBitmapFromAsests(activity, MainActivity.pictureName, 100, 100)
                else
                    thumbImg = Bitmap.createScaledBitmap(bitmap, 100, 100, false)

                if (thumbImg == null)
                    return

                ThumbnailsManager.clearThumbs()
                thumbnailList!!.clear()
                var thumbnailItem = ThumbnailItem()
                thumbnailItem.image = thumbImg
                thumbnailItem.filterName = "Normal"
                ThumbnailsManager.addThumb(thumbnailItem)
                var filters = FilterPack.getFilterPack(activity) as MutableList<Filter>
                for (f in filters) {
                    var thumbnailItem = ThumbnailItem()
                    thumbnailItem.image = thumbImg
                    thumbnailItem.filter=f
                    thumbnailItem.filterName = f.name
                    ThumbnailsManager.addThumb(thumbnailItem)
                    Log.d("filterpack", f.name)
                }
                thumbnailList!!.addAll(ThumbnailsManager.processThumbs(activity))
                activity?.runOnUiThread(object : Runnable {
                    override fun run() {
                        thumbnailAdapter.notifyDataSetChanged()
                    }
                })
            }
        }
        Thread(runnable).start()
    }

    override fun onFilterSelected(filter: Filter) {
        if (listener != null) {
            listener.onFilterSelected(filter)
        } else {
            return
        }

    }

    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


}
