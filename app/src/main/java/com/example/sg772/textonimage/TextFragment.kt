package com.example.sg772.textonimage

import android.os.Bundle
import android.graphics.Color
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.sg772.textonimage.Adapter.ColorAdapter
import com.example.sg772.textonimage.Interfaces.textOnImageListener
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TextFragment : BottomSheetDialogFragment(), ColorAdapter.ColorAdapterListener {


    lateinit var color_list:RecyclerView
    lateinit var color_adapter: ColorAdapter
    lateinit var text_input:EditText
    lateinit var done:Button
    var addTextLIstener: textOnImageListener?=null
var selectedColor=Color.parseColor("#000000")
    companion object {
        lateinit var instance:TextFragment
        open fun newInstance():TextFragment{

            instance= TextFragment()
            return instance
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View= inflater.inflate(R.layout.fragment_text, container, false)
        text_input=view.findViewById(R.id.text_input)
        done=view.findViewById(R.id.text_input_done_button)
        color_list=view.findViewById(R.id.colors_list)
        color_adapter = ColorAdapter( BrushFragment.genColors(),activity, this)
        color_list.layoutManager = LinearLayoutManager(context,LinearLayout.HORIZONTAL,false)
        color_list.adapter = color_adapter
        done.setOnClickListener {
            addTextLIstener?.adTextListener(text_input.text.toString(),selectedColor)
        }
    return view
    }

    override fun onColorSelected(color: Int) {
selectedColor=color
    }



}
