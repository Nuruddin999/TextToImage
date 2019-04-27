package com.example.sg772.textonimage

import android.app.Dialog
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.sg772.textonimage.Adapter.ColorAdapter
import com.example.sg772.textonimage.Adapter.FontAdapter
import com.example.sg772.textonimage.Interfaces.textOnImageListener
import java.lang.StringBuilder
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TextFragment : DialogFragment(), ColorAdapter.ColorAdapterListener, FontAdapter.fontClickListener {


    lateinit var color_list: RecyclerView
    lateinit var color_adapter: ColorAdapter
    lateinit var text_input: EditText
    lateinit var done: Button
    lateinit var font_list: RecyclerView
    lateinit var font_adaper: FontAdapter
    var typeface: Typeface= Typeface.DEFAULT
    var addTextLIstener: textOnImageListener? = null
    var selectedColor = Color.parseColor("#000000")

    companion object {
        lateinit var instance: TextFragment
        open fun newInstance(): TextFragment {

            instance = TextFragment()
            return instance
        }
    }

    override fun onStart() {
        super.onStart()
        var dialog:Dialog=dialog
        if (dialog!=null){
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_text, container, false)
        var width = ViewGroup.LayoutParams.MATCH_PARENT
        var height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.setLayout(width, height)
        dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        text_input = view.findViewById(R.id.text_input)
        done = view.findViewById(R.id.text_input_done_button)
        font_list = view.findViewById(R.id.font_list)
        font_adaper = FontAdapter(context!!, this)
        font_list.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        font_list.adapter = font_adaper
        color_list = view.findViewById(R.id.colors_list)
        color_adapter = ColorAdapter(BrushFragment.genColors(), activity, this)
        color_list.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        color_list.adapter = color_adapter
        done.setOnClickListener {
                addTextLIstener?.adTextListener(typeface!!, text_input.text.toString(), selectedColor)
                dismiss()

        }
        return view
    }


    override fun onColorSelected(color: Int) {
        selectedColor = color
        text_input.setTextColor(color)
    }

    override fun onFontSelected(font: String) {
        typeface = Typeface.createFromAsset(context?.assets, StringBuilder("fonts/").append(font).toString())
        text_input.typeface = typeface
    }


}
