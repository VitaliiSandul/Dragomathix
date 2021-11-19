package com.sandul.mathtable

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class TablesFragment : Fragment(), View.OnClickListener{

    private lateinit var mAdViewTable: AdView
    private lateinit var clTables: ConstraintLayout
    private lateinit var clMain: ConstraintLayout
    private lateinit var buttons: Array<ImageButton>
    private var result1: TextView? = null
    private var result2: TextView? = null
    private var answer: Int = 0
    private lateinit var switchButton: androidx.appcompat.widget.SwitchCompat
    private lateinit var backButton1: ImageButton
    private var isShow: Boolean = true
    private var curIndex: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        if (container == null)
            return null

        val v = inflater.inflate(R.layout.fragment_tables, container, false)

        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        initElems(v)
        setBg(MainActivity.curGender)
        fillTableFragment(0)
        clickBtns()

        switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                isShow = true
            }else{
                isShow = false
            }
            fillTableFragment(curIndex)
        }

        adBannerAdvertToTablesFrag(v)
        return v
    }

    private fun adBannerAdvertToTablesFrag(v: View?) {
        mAdViewTable = v!!.findViewById(R.id.adViewTable)
        val adRequest = AdRequest.Builder().build()
        mAdViewTable.loadAd(adRequest)
    }

    private fun setBg(gender: String) {
        if(gender.equals("boy")){
            clTables.setBackgroundResource(R.drawable.back_boy)
        }
        else{
            clTables.setBackgroundResource(R.drawable.back_girl)
        }
    }

    private fun initElems(v: View?) {
        clTables = v?.findViewById(R.id.cl_tables)!!
        clMain = v?.findViewById(R.id.clmain)!!
        result1 = v?.findViewById(R.id.wtext1)
        result2 = v?.findViewById(R.id.wtext2)
        switchButton = v?.findViewById(R.id.switch_button)!!
        backButton1 = v?.findViewById(R.id.back_button1)!!

        buttons = arrayOf<ImageButton>(v!!.findViewById<ImageButton>(R.id.test_0), v.findViewById<ImageButton>(R.id.test_1), v.findViewById<ImageButton>(R.id.test_2),
                                        v.findViewById<ImageButton>(R.id.test_3), v.findViewById<ImageButton>(R.id.test_4), v.findViewById<ImageButton>(R.id.test_5),
                                        v.findViewById<ImageButton>(R.id.test_6), v.findViewById<ImageButton>(R.id.test_7), v.findViewById<ImageButton>(R.id.test_8),
                                        v.findViewById<ImageButton>(R.id.test_9), v.findViewById<ImageButton>(R.id.test_10), v.findViewById<ImageButton>(R.id.test_11))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.test_0 -> curIndex = 0
            R.id.test_1 -> curIndex = 1
            R.id.test_2 -> curIndex = 2
            R.id.test_3 -> curIndex = 3
            R.id.test_4 -> curIndex = 4
            R.id.test_5 -> curIndex = 5
            R.id.test_6 -> curIndex = 6
            R.id.test_7 -> curIndex = 7
            R.id.test_8 -> curIndex = 8
            R.id.test_9 -> curIndex = 9
            R.id.test_10 -> curIndex = 10
            R.id.test_11 -> curIndex = 11
            R.id.back_button1 -> activity?.onBackPressed()
        }
        fillTableFragment(curIndex)
    }

    private fun clickBtns(){
        for (btn in buttons) {
            btn.setOnClickListener(this)
        }
        backButton1.setOnClickListener(this)
    }

    private fun buildTable(index: Int) {

        val builder1 = SpannableStringBuilder()
        val builder2 = SpannableStringBuilder()

        var i = 0
            while (i <= 11) {
                answer = i * index

                if(i <=5) {
                    val unicode = 0x2B50
                    builder1.append("$index X $i = ")
                    if(isShow){
                        builder1.append("$answer")
                    }
                    else{
                        builder1.append("?")
                    }

                    if(i!=5){
                        builder1.append("\n\n")
                    }
                }
                else{
                    builder2.append("$index X $i = ")
                    if(isShow){
                        builder2.append("$answer")
                    }
                    else{
                        builder2.append("?")
                    }

                    if(i!=11){
                        builder2.append("\n\n")
                    }
                }
                i++
            }

        result1!!.text = builder1
        result2!!.text = builder2
    }

    private fun paintButtons(index: Int) {
        for ((pos, btn) in buttons.withIndex()) {

            if(pos == index){
                btn.setBackgroundResource(R.drawable.btn_accept)
            }else{
                btn.setBackgroundResource(R.drawable.btn_calc)
            }
        }
    }

    private fun fillTableFragment(index: Int){
        buildTable(index)
        paintButtons(index)
    }
}