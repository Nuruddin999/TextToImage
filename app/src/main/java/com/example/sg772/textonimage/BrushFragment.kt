package com.example.sg772.textonimage


import android.app.Dialog
import android.os.Bundle
import android.app.Fragment
import android.graphics.Color
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.ToggleButton
import com.example.sg772.textonimage.Adapter.ColorAdapter
import com.example.sg772.textonimage.Interfaces.BrushListener
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BrushFragment : BottomSheetDialogFragment(), ColorAdapter.ColorAdapterListener {


    lateinit var seekbar_brushsize: SeekBar
    lateinit var seekbar_brushpoacity: SeekBar
    lateinit var seekbar_saturation: SeekBar
    lateinit var color_list: RecyclerView
    lateinit var brush_button: ToggleButton
    var listener: BrushListener? = null
    lateinit var color_adapter: ColorAdapter

    companion object {
        fun genColors(): List<Int>? {

/*
        #fff380
        #0000ff
        #ff4d00
        #afb169
        #afaf0a
*/
            var colorList = ArrayList<Int>()
            colorList.add(Color.parseColor("#5a7dd0"))
            colorList.add(Color.parseColor("#fff000"))
            colorList.add(Color.parseColor("#00a90a"))
            colorList.add(Color.parseColor("#80a32d"))
            colorList.add(Color.parseColor("#787879"))
            colorList.add(Color.parseColor("#808cff"))
            colorList.add(Color.parseColor("#fff380"))
            colorList.add(Color.parseColor("#df2e90"))
            colorList.add(Color.parseColor("#543683"))
            colorList.add(Color.parseColor("#fffff2"))
            colorList.add(Color.parseColor("#47eabc"))
            colorList.add(Color.parseColor("#d20057"))
            colorList.add(Color.parseColor("#515e63"))
            colorList.add(Color.parseColor("#497147"))
            colorList.add(Color.parseColor("#376034"))
            colorList.add(Color.parseColor("#087264"))
            colorList.add(Color.parseColor("#4ca6ff"))
            colorList.add(Color.parseColor("#460a18"))
            colorList.add(Color.parseColor("#faebd7"))
            colorList.add(Color.parseColor("#eed330"))
            colorList.add(Color.parseColor("#cb4bca"))
            colorList.add(Color.parseColor("#fffff2"))
            colorList.add(Color.parseColor("#fffc3c"))
            colorList.add(Color.parseColor("#b507db"))
            colorList.add(Color.parseColor("#ff8d00"))
            colorList.add(Color.parseColor("#00ff38"))
            colorList.add(Color.parseColor("#ff00e7"))
            colorList.add(Color.parseColor("#98bdf0"))
            colorList.add(Color.parseColor("#daf7f8"))
            colorList.add(Color.parseColor("#b9e0d9"))

            colorList.add(Color.parseColor("#debcb0"))
            colorList.add(Color.parseColor("#ffb3b3"))
            colorList.add(Color.parseColor("#b8d9d0"))
            colorList.add(Color.parseColor("#83d0f2"))
            colorList.add(Color.parseColor("#552f2e"))

            return colorList
        }
        lateinit var instance:BrushFragment
        open fun newInstance():BrushFragment{

            instance=BrushFragment()
            return instance
        }
    }
    override fun onStart() {
        super.onStart()
        var dialog: Dialog =dialog
        if (dialog!=null){
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_brush2, container, false)
        seekbar_brushsize = view.findViewById(R.id.seekbar_brush_size)
        seekbar_brushpoacity = view.findViewById(R.id.seekbar_opacity)
        color_list = view.findViewById(R.id.color_list)
        brush_button = view.findViewById(R.id.brush_button)
        color_adapter = ColorAdapter(genColors(),activity, this)
        color_list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        color_list.adapter = color_adapter
        seekbar_brushsize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener!!.brushSizeChangeListener(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        seekbar_brushpoacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listener?.brushOpacityChangeListener(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
brush_button.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        listener?.brushStateChangeListener(isChecked)
    }
})
        return view
    }



    override fun onColorSelected(color: Int) {
        listener?.brushColorChangeListener(color)
    }



}
