package com.example.sg772.textonimage.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.sg772.textonimage.Interfaces.FiltersFragmentListener
import com.example.sg772.textonimage.R
import com.zomato.photofilters.utils.ThumbnailItem
import kotlinx.android.synthetic.main.thumbnail_item.view.*

class ThumbnailAdapter: RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {
lateinit var thumbnailItem: List<ThumbnailItem>
    lateinit var listener: FiltersFragmentListener
    lateinit var context: Context
    var selectedIndex: Int?=0

    constructor(thumbnailItem: List<ThumbnailItem>, listener: FiltersFragmentListener, context: Context) : super() {
        this.thumbnailItem = thumbnailItem
        this.listener = listener
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
 var itemview: View=LayoutInflater.from(context).inflate(R.layout.thumbnail_item,parent,false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return  thumbnailItem.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
    var thumbnailIt: ThumbnailItem=thumbnailItem.get(p1)
        p0.thumbnail.setImageBitmap(thumbnailIt.image)
        p0.thumbnail.setOnClickListener {
            listener.onFilterSelected(thumbnailIt.filter)
            selectedIndex=p1
            notifyDataSetChanged()
        }
p0.filterName.setText(thumbnailIt.filterName)
        if (selectedIndex==p1){
            p0.filterName.setTextColor(ContextCompat.getColor(context,R.color.selected_filter))
        }
        else {
            p0.filterName.setTextColor(ContextCompat.getColor(context,R.color.normal_filter))

        }
    }


    class MyViewHolder : RecyclerView.ViewHolder {

        lateinit var thumbnail: ImageView
        lateinit var filterName:TextView

constructor(itemView: View): super(itemView){
    thumbnail=itemView.findViewById(R.id.thumbnail_name)
filterName=itemView.findViewById(R.id.filter_name)
}

    }


}