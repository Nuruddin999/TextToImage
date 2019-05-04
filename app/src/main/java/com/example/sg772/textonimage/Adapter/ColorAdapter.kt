package com.example.sg772.textonimage.Adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sg772.textonimage.R

open class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    var colors: List<Int>? = null
    var context: Context? = null
 var colorListener: ColorAdapterListener?=null

    constructor(colors: List<Int>?, context: Context?, colorListener: ColorAdapterListener)  {
        this.colors = colors
        this.context = context
        this.colorListener = colorListener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ColorViewHolder {
var itemview: View=LayoutInflater.from(context).inflate(R.layout.color_item,p0,false)
        return ColorViewHolder(itemview)
    }

    override fun getItemCount(): Int {
return colors!!.size
    }

    override fun onBindViewHolder(p0: ColorViewHolder, p1: Int) {
p0.colorSection.setCardBackgroundColor(colors!!.get(p1))
        p0.colorSection.setOnClickListener(object:  View.OnClickListener{
            override fun onClick(v: View?) {
                colorListener?.onColorSelected(colors!!.get(p1))
            }
        })
    }


    open class ColorViewHolder : RecyclerView.ViewHolder {
        lateinit var colorSection: CardView

        constructor (itemView: View) : super(itemView) {
            colorSection = itemView.findViewById(R.id.color_item)



        }


    }


    open interface ColorAdapterListener {
        open fun onColorSelected(color: Int)
    }
}



