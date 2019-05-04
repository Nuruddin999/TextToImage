package com.example.sg772.textonimage.Adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.sg772.textonimage.R
import kotlinx.android.synthetic.main.font_item.view.*
import java.lang.StringBuilder
import java.util.ArrayList

class FontAdapter(internal var context: Context, internal var listener: FontAdapter.fontClickListener) :
    RecyclerView.Adapter<FontAdapter.FontViewHolder>() {
    internal var font_list: List<String>
var row_selected=-1
    init {
        this.font_list = loadfontlist()

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FontViewHolder {
        var itemview: View= LayoutInflater.from(context).inflate(R.layout.font_item,p0,false)
        return FontViewHolder (itemview)
    }

    override fun getItemCount(): Int {
        return font_list.size
    }

    override fun onBindViewHolder(p0: FontViewHolder, p1: Int) {
        p0.demo_logo.typeface=Typeface.createFromAsset(context.assets,StringBuilder("fonts/").append(font_list.get(p1)).toString())
        p0.fontsection.setOnClickListener {
            listener.onFontSelected(font_list.get(p1))
            var typerface=Typeface.createFromAsset(context.assets,StringBuilder("fonts/").append(font_list.get(p1)).toString())

            row_selected=p1
            notifyDataSetChanged()
        }
        if (row_selected==p1){
            p0.font_checked.visibility=View.VISIBLE
        } else {
            p0.font_checked.visibility=View.INVISIBLE
        }

        p0.font_name.text=font_list.get(p1)

    }


    private fun loadfontlist(): List<String> {
        var fonts = ArrayList<String>()
fonts.add("ABeeZee-Italic.ttf")
        fonts.add("ABeeZee-Regular.ttf")
        fonts.add("Abel-Regular.ttf")
        fonts.add("AbrilFatface-Regular.ttf")
        fonts.add("Acme-Regular.ttf")
        fonts.add("Actor-Regular.ttf")

        return fonts
    }

    open interface fontClickListener {
        fun onFontSelected(font: String)
    }

    inner class FontViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        lateinit var demo_logo: TextView
        lateinit var font_name: TextView
        lateinit var font_checked: ImageView
        lateinit var fontsection:CardView

        init {
            demo_logo = itemview.findViewById(R.id.font_demo_logo)
            font_name = itemview.findViewById(R.id.font_name)
            font_checked = itemview.findViewById(R.id.font_check)
            fontsection=itemview.findViewById(R.id.font_item_view)
        }

    }
}