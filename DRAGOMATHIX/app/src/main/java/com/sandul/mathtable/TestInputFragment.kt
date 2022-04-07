package com.sandul.mathtable

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class TestInputFragment : Fragment(), View.OnClickListener, ChooseMultiplierDialogFragment.OnInputSelected {

    private lateinit var mAdViewTestInput: AdView
    private lateinit var clTest: ConstraintLayout
    private lateinit var btnCalc0: ImageButton
    private lateinit var btnCalc1: ImageButton
    private lateinit var btnCalc2: ImageButton
    private lateinit var btnCalc3: ImageButton
    private lateinit var btnCalc4: ImageButton
    private lateinit var btnCalc5: ImageButton
    private lateinit var btnCalc6: ImageButton
    private lateinit var btnCalc7: ImageButton
    private lateinit var btnCalc8: ImageButton
    private lateinit var btnCalc9: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var btnAccept: ImageButton
    private lateinit var backButton3: ImageButton
    private lateinit var qTxt: TextView
    private lateinit var aTxt: TextView
    private lateinit var toastMsg: TextView
    private lateinit var txtMultiplier: TextView
    private lateinit var txtTestQuest: TextView
    private var questionCount: Int = 0
    private var correctAnswerCount: Int = 0
    private var testLevel: Int = 0

    private val tableViewModel: TableViewModel by lazy {
        ViewModelProviders.of(this).get(TableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        val dialog = ChooseMultiplierDialogFragment(1, 10)
        dialog.setTargetFragment(this@TestInputFragment, 1)
        fragmentManager?.let { dialog.show(it, "ChooseMultiplierDialogFragment") }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        if (container == null)
            return null

        val view = inflater.inflate(R.layout.fragment_test_input, container, false)

        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        initButtons(view)
        setBg(MainActivity.curGender)
        clickBtns()

        adBannerAdvertToTestInputFrag(view)

        return view
    }

    private fun adBannerAdvertToTestInputFrag(v: View?) {
        mAdViewTestInput = v!!.findViewById(R.id.adViewTestInput)
        val adRequest = AdRequest.Builder().build()
        mAdViewTestInput.loadAd(adRequest)
    }

    private fun refreshQuestion() {
        qTxt.text = tableViewModel.currentQuestionText
        aTxt.text = ""
    }

    private fun setFirstQuestion(){
        while (tableViewModel.currentQuestionCategory != testLevel){
            tableViewModel.moveToNext()
        }
    }

    private fun initButtons(view: View?) {
        clTest = view?.findViewById<ConstraintLayout>(R.id.cl_test)!!
        btnCalc0 = view?.findViewById<ImageButton>(R.id.btn_calc_0)!!
        btnCalc1 = view?.findViewById<ImageButton>(R.id.btn_calc_1)!!
        btnCalc2 = view?.findViewById<ImageButton>(R.id.btn_calc_2)!!
        btnCalc3 = view?.findViewById<ImageButton>(R.id.btn_calc_3)!!
        btnCalc4 = view?.findViewById<ImageButton>(R.id.btn_calc_4)!!
        btnCalc5 = view?.findViewById<ImageButton>(R.id.btn_calc_5)!!
        btnCalc6 = view?.findViewById<ImageButton>(R.id.btn_calc_6)!!
        btnCalc7 = view?.findViewById<ImageButton>(R.id.btn_calc_7)!!
        btnCalc8 = view?.findViewById<ImageButton>(R.id.btn_calc_8)!!
        btnCalc9 = view?.findViewById<ImageButton>(R.id.btn_calc_9)!!
        btnDelete = view?.findViewById<ImageButton>(R.id.btn_delete)!!
        btnAccept = view?.findViewById<ImageButton>(R.id.btn_accept)!!
        backButton3 = view?.findViewById<ImageButton>(R.id.back_button3)!!
        qTxt = view?.findViewById<TextView>(R.id.q_txt)!!
        aTxt = view?.findViewById<TextView>(R.id.a_txt)!!
        toastMsg = view?.findViewById<TextView>(R.id.toast_msg)!!
        txtMultiplier = view?.findViewById<TextView>(R.id.txt_multiplier)!!
        txtTestQuest = view?.findViewById<TextView>(R.id.txt_test_quest)!!
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_calc_0 -> {
                addTextToATxt("0")
            }
            R.id.btn_calc_1 -> {
                addTextToATxt("1")
            }
            R.id.btn_calc_2 -> {
                addTextToATxt("2")
            }
            R.id.btn_calc_3 -> {
                addTextToATxt("3")
            }
            R.id.btn_calc_4 -> {
                addTextToATxt("4")
            }
            R.id.btn_calc_5 -> {
                addTextToATxt("5")
            }
            R.id.btn_calc_6 -> {
                addTextToATxt("6")
            }
            R.id.btn_calc_7 -> {
                addTextToATxt("7")
            }
            R.id.btn_calc_8 -> {
                addTextToATxt("8")
            }
            R.id.btn_calc_9 -> {
                addTextToATxt("9")
            }
            R.id.btn_delete -> {
                var strTxt: String = aTxt.text.toString()
                if (strTxt.length > 0) {
                    strTxt = strTxt.substring(0, strTxt.length - 1);
                    aTxt.text = strTxt
                }
            }
            R.id.btn_accept -> {
                if (questionCount < 10) {
                    unclickBtns()
                    questionCount++

                    if (aTxt.text == tableViewModel.currentQuestionAnswer.toString()) {
                        toastMsg.text = getString(R.string.correct_answer)
                        toastMsg.setTextColor(
                            ContextCompat.getColor(
                                activity?.applicationContext!!,
                                R.color.light_green
                            )
                        )
                        toastMsg.visibility = View.VISIBLE
                        correctAnswerCount++

                        var newList = MainActivity.curProgress.toMutableList()
                        if (newList.get(tableViewModel.currentQuestionCategory-1) < 100) {
                            newList.set(
                                tableViewModel.currentQuestionCategory-1,
                                newList.get(tableViewModel.currentQuestionCategory-1) + 1
                            )
                            MainActivity.curProgress = newList.toList()
                            MainActivity.instance.updateProgress()
                            MainActivity.instance.updatePlayerSettingsFun()
                        }

                        Log.d("curProgress", MainActivity.curProgress.toString())
                    } else {

                        toastMsg.text = getString(R.string.incorrect_answer)
                        toastMsg.setTextColor(
                            ContextCompat.getColor(
                                activity?.applicationContext!!,
                                R.color.red
                            )
                        )
                        toastMsg.visibility = View.VISIBLE
                    }

                    Utils.runOnUiThread {
                        val handler = Handler()
                        handler.postDelayed({
                            toastMsg.visibility = View.GONE

                            if (questionCount < 10) {
                                txtTestQuest.text = (questionCount + 1).toString() + "/10"
                                tableViewModel.moveToNext()
                                refreshQuestion()
                            } else {
                                var percent = correctAnswerCount * 100 / 10

                                val d = ResultDialogFragment()
                                val b = Bundle()
                                b.putInt("KEY1", percent)
                                d.arguments = b
                                d.setTargetFragment(this@TestInputFragment, 1)
                                fragmentManager?.let { d.show(it, "ChooseTestDialogFragment") }
                            }
                            clickBtns()
                        }, 1500)
                    }

                }
            }
            R.id.back_button3 -> {
                activity?.onBackPressed()
            }
        }
    }

    private fun addTextToATxt(s: String) {
        var str: String = aTxt.text.toString()
        if(str.length < 3){
            str += s
            if(str.startsWith("0") && str.length > 1){
                str = str.substring(0, str.length - 1)
            }
            aTxt.text = str
        }
    }

    private fun clickBtns(){
        btnCalc0.setOnClickListener(this)
        btnCalc1.setOnClickListener(this)
        btnCalc2.setOnClickListener(this)
        btnCalc3.setOnClickListener(this)
        btnCalc4.setOnClickListener(this)
        btnCalc5.setOnClickListener(this)
        btnCalc6.setOnClickListener(this)
        btnCalc7.setOnClickListener(this)
        btnCalc8.setOnClickListener(this)
        btnCalc9.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnAccept.setOnClickListener(this)
        backButton3.setOnClickListener(this)
    }

    private fun unclickBtns(){
        btnCalc0.setOnClickListener(null)
        btnCalc1.setOnClickListener(null)
        btnCalc2.setOnClickListener(null)
        btnCalc3.setOnClickListener(null)
        btnCalc4.setOnClickListener(null)
        btnCalc5.setOnClickListener(null)
        btnCalc6.setOnClickListener(null)
        btnCalc7.setOnClickListener(null)
        btnCalc8.setOnClickListener(null)
        btnCalc9.setOnClickListener(null)
        btnDelete.setOnClickListener(null)
        btnAccept.setOnClickListener(null)
        backButton3.setOnClickListener(null)
    }

    override fun sendLevel(level: Int) {
        testLevel = level
        txtMultiplier.text = txtMultiplier.text.toString() + " x" + level
        setFirstQuestion()
        refreshQuestion()
    }

    private fun setBg(gender: String) {
        if(gender.equals("boy")){
            clTest.setBackgroundResource(R.drawable.back_boy)
        }
        else{
            clTest.setBackgroundResource(R.drawable.back_girl)
        }
    }
}