package com.sandul.mathtable

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import org.jetbrains.anko.doAsync

class ResultDialogFragment : DialogFragment(){

    private lateinit var clRes: ConstraintLayout
    private lateinit var yourResultTxt: TextView
    private lateinit var imgRes: ImageView
    private lateinit var msgTxt: TextView
    private lateinit var negativeBtn: Button
    private lateinit var positiveBtn: Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var v: View = inflater.inflate(R.layout.fragment_result_dialog, container, false)
        initResElem(v)
        setBg(MainActivity.curGender)
        val arg = arguments

        if(arg?.get("KEY1") != null){
            msgTxt.textSize = 35f
            msgTxt.text = ""+ (arg?.get("KEY1") ?: "0") + "% "
            negativeBtn.text = getString(R.string.repeat)
            positiveBtn.text = getString(R.string.finish)

            negativeBtn.setOnClickListener {
                activity?.onBackPressed()
                val ft: FragmentTransaction = activity?.getSupportFragmentManager()!!.beginTransaction()
                ft.replace(R.id.container, TestInputFragment(), "testtag")
                        .addToBackStack(null)
                        .commit()
                dismiss()
            }
            positiveBtn.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                dismiss()
            }

        }
        else if(arg?.get("KEY2") != null){
            msgTxt.textSize = 35f
            msgTxt.text = ""+ (arg?.get("KEY2") ?: "")
            negativeBtn.text = getString(R.string.repeat)
            positiveBtn.text = getString(R.string.finish)


            if(msgTxt.text.toString().equals(getString(R.string.you_win))){
                imgRes.setImageResource(R.drawable.win_cup)
            }else if(msgTxt.text.toString().equals(getString(R.string.you_lose))){
                imgRes.setImageResource(R.drawable.lose_face)
            }else if(msgTxt.text.toString().equals(getString(R.string.draw_in_the_race))){
                imgRes.setImageResource(R.drawable.draw_hands)
            }

            imgRes.visibility = View.VISIBLE

            Log.d("KEY2",""+ (arg?.get("KEY2") ?: ""))
            Log.d("KEY2", getString(R.string.you_win))
            Log.d("KEY2", getString(R.string.you_lose))
            Log.d("KEY2", getString(R.string.draw_in_the_race))

            negativeBtn.setOnClickListener {
                imgRes.visibility = View.GONE
                activity?.supportFragmentManager?.popBackStack()
                val ft: FragmentTransaction = activity?.getSupportFragmentManager()!!.beginTransaction()
                ft.replace(R.id.container, HippodromeFragment(), "hippodrometag")
                        .addToBackStack(null)
                        .commit()
                dismiss()
            }
            positiveBtn.setOnClickListener {
                imgRes.visibility = View.GONE
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                dismiss()
            }
        }
        else if(arg?.get("KEY3") != null){
            yourResultTxt.visibility = View.GONE
            msgTxt.textSize = 35f
            msgTxt.text = ""+ (arg?.get("KEY3") ?: "")
            negativeBtn.text = getString(R.string.no)
            positiveBtn.text = getString(R.string.yes)

            negativeBtn.setOnClickListener {
                dismiss()
            }
            positiveBtn.setOnClickListener {
                doAsync {
                    MainActivity.curProgress = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                    MainActivity.instance.updatePlayerSettingsFun()
                }

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                dismiss()
            }
        }
        else if(arg?.get("KEY4") != null){
            yourResultTxt.visibility = View.GONE
            msgTxt.textSize = 20f
            msgTxt.text = "" + getString(R.string.you_didnt_train) + "\n" + arg?.get("KEY4") + " "+ getString(R.string.days) + ". \n" + getString(R.string.your_progress_has_decreased)
            negativeBtn.visibility = View.GONE
            positiveBtn.text = "Ok"

            positiveBtn.setOnClickListener {
                dismiss()
            }
        }
        else if(arg?.get("KEY5") != null){
            yourResultTxt.visibility = View.GONE
            msgTxt.textSize = 35f
            msgTxt.text = ""+ (arg?.get("KEY5") ?: "")
            negativeBtn.text = getString(R.string.no)
            positiveBtn.text = getString(R.string.yes)

            negativeBtn.setOnClickListener {
                dismiss()
            }
            positiveBtn.setOnClickListener {
                activity?.finishAffinity()
            }
        }
        return v
    }

    private fun initResElem(view: View?){
        clRes = view?.findViewById<ConstraintLayout>(R.id.cl_res)!!
        yourResultTxt = view?.findViewById<TextView>(R.id.your_result_txt)!!
        imgRes = view?.findViewById<ImageView>(R.id.img_res)!!
        msgTxt = view?.findViewById<TextView>(R.id.msg_txt)!!
        negativeBtn = view?.findViewById<Button>(R.id.negative_btn)!!
        positiveBtn = view?.findViewById<Button>(R.id.positive_btn)!!
    }

    private fun setBg(gender: String) {
        if(gender.equals("boy")){
            clRes.setBackgroundResource(R.color.light_light_blue)
        }
        else{
            clRes.setBackgroundResource(R.color.light_light_pink)
        }
    }
}