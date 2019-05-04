package com.example.sg772.textonimage

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import com.example.sg772.textonimage.Interfaces.EditImageFragmentListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditImageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EditImageFragment() : BottomSheetDialogFragment(), SeekBar.OnSeekBarChangeListener {


    // TODO: Rename and change types of parameters

    // private var listener: OnFragmentInteractionListener? = null
   var listener: EditImageFragmentListener?=null
    lateinit var seekbar_brightness: SeekBar
    lateinit var seekbar_constrants: SeekBar
    lateinit var seekbar_saturation: SeekBar

    companion object {
        lateinit var instance:EditImageFragment
        open fun newInstance():EditImageFragment{

                      instance= EditImageFragment()
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
        var itemView: View = inflater.inflate(R.layout.fragment_edit_image, container, false)
        seekbar_brightness = itemView.findViewById(R.id.seekbar_brightness)
        seekbar_constrants = itemView.findViewById(R.id.seekbar_constraint)
        seekbar_saturation = itemView.findViewById(R.id.seekbar_saturation)
        seekbar_brightness.max = 200
        seekbar_brightness.setProgress(100)
        seekbar_constrants.max = 20
        seekbar_constrants.setProgress(0)
        seekbar_saturation.max = 30
        seekbar_saturation.setProgress(10)
        seekbar_brightness.setOnSeekBarChangeListener(this)
        seekbar_constrants.setOnSeekBarChangeListener(this)
        seekbar_saturation.setOnSeekBarChangeListener(this)
        return itemView
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
       var progress=progress
        if (listener != null) {
            if (seekBar!!.id == R.id.seekbar_brightness) {
                listener?.onBrightnessChanged(progress - 100)
            }else if (seekBar.id==R.id.seekbar_constraint){
<<<<<<< HEAD
                progress+=10
                var float=.10f*progress
                listener.onConstrantChanged(float)
=======
                var mean=progress+10
                var float=.10f*mean
                listener?.onConstrantChanged(float)
>>>>>>> bottomNav
            } else if (seekBar.id==R.id.seekbar_saturation){
                var float=.10f*progress
                listener?.onSaturationChanged(float)
            }


            /*    when (seekBar?.id) {
                    R.id.seekbar_brightness -> listener.onBrightnessChanged(progress - 100)
                    R.id.seekbar_constraint -> {
                            progress+=10
                        var value: Float = .10f * progress
                        listener.onConstrantChanged(value)
                    }
                    R.id.seekbar_saturation -> {
                        var value: Float = .10f * progress
                        listener.onSaturationChanged( value)
                    }

                }*/
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        if (listener != null) {
            listener?.onEditStarted()
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (listener != null) {
            listener?.onEditCompleted()
        }
    }

    fun resetControlls() {
        seekbar_brightness.setProgress(100)
        seekbar_constrants.setProgress(0)
        seekbar_saturation.setProgress(10)
    }
}
