package com.sandul.mathtable

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import java.util.*


class ChooseMultiplierDialogFragment(val a: Int, val b: Int) : DialogFragment() {

    interface OnInputSelected {
        fun sendLevel(input: Int)
    }

    private val TAG = "ChooseMultiplierDialogFragment"
    private lateinit var cl: ConstraintLayout
    private lateinit var rg1: RadioGroup
    private lateinit var rg2: RadioGroup
    private lateinit var cancelBtn: Button
    private lateinit var acceptBtn: Button
    var mOnInputSelected: OnInputSelected? = null

    private var level = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mOnInputSelected = targetFragment as OnInputSelected?
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException : " + e.message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v: View = inflater.inflate(R.layout.fragment_choose_multiplier_dialog, container, false)

        var size = b-a+1
        initRBtns(v)
        setBg(MainActivity.curGender)
        createRadioButton(size)
        rg1.setOnCheckedChangeListener(listener)
        rg2.setOnCheckedChangeListener(listener)
        cancelBtn.setOnClickListener {
            mOnInputSelected?.sendLevel(0)
            dismiss()
        }
        acceptBtn.setOnClickListener {
            mOnInputSelected?.sendLevel(level)
            dismiss()
        }

        return v
    }

    private fun initRBtns(v: View?) {
        cl = v?.findViewById<ConstraintLayout>(R.id.cl)!!
        rg1 = v?.findViewById<RadioGroup>(R.id.rg1)!!
        rg2 = v?.findViewById<RadioGroup>(R.id.rg2)!!
        cancelBtn = v?.findViewById<Button>(R.id.cancel_btn)!!
        acceptBtn = v?.findViewById<Button>(R.id.accept_btn)!!
    }

    private fun createRadioButton(size: Int) {
        val rb = arrayOfNulls<RadioButton>(size)

        var index = 0
        for (i in a..b) {
            rb[index] = RadioButton(activity)
            rb[index]!!.textSize = 22F
            rb[index]!!.text = i.toString()
            rb[index]!!.id = index

            var half = size/2 + size % 2
            if(index < half){
                rg1.addView(rb[index])
            }
            else{
                rg2.addView(rb[index])
            }
            index++
        }
        rb[0]!!.isChecked = true
    }

    private val listener: RadioGroup.OnCheckedChangeListener = object : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

            var otherGroup: RadioGroup = rg1
            if(group == rg1){
                otherGroup = rg2
            }
            otherGroup.setOnCheckedChangeListener(null)
            otherGroup.clearCheck()
            otherGroup.setOnCheckedChangeListener(this)

            level = checkedId
            Log.d("---level---", level.toString())
        }
    }

    private fun setBg(gender: String) {
        if(gender.equals("boy")){
            cl.setBackgroundResource(R.color.light_light_blue)
        }
        else{
            cl.setBackgroundResource(R.color.light_light_pink)
        }
    }
}