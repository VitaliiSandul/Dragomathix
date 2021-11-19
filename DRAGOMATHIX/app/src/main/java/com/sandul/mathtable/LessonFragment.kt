package com.sandul.mathtable

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import java.util.*

class LessonFragment : Fragment(), View.OnClickListener, ChooseMultiplierDialogFragment.OnInputSelected {

    var level: Int = 1
    private lateinit var viewFlipper: ViewFlipper
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var backButton2: ImageButton

    private var listTitle: List<Int>? = null
    private var listImages: List<Int>? = null
    private var listTxt1: List<Int>? = null
    private var listTxt2: List<Int>? = null
    private var listTimes: List<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        val dialog = ChooseMultiplierDialogFragment(1, 10)
        dialog.setTargetFragment(this@LessonFragment, 1)
        fragmentManager?.let { dialog.show(it, "lessonDialogFragment") }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (container == null)
            return null

        val v = inflater.inflate(R.layout.fragment_lesson, container, false)
        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        initElem(v)
        clickBtns()

        return v
    }

    private fun clickBtns() {
        prevButton.setOnClickListener(this)
        nextButton.setOnClickListener(this)
        backButton2.setOnClickListener(this)
    }

    private fun initElem(v: View?) {
        viewFlipper = v?.findViewById<ViewFlipper>(R.id.view_flipper)!!
        prevButton = v?.findViewById<ImageButton>(R.id.prev_button)!!
        nextButton = v?.findViewById<ImageButton>(R.id.next_button)!!
        backButton2 = v?.findViewById<ImageButton>(R.id.back_button2)!!
    }

    override fun sendLevel(input: Int) {
        level = input + 1
        setViewFlipper()
    }

    private fun setViewFlipper(){
        listTitle = ArrayList(Arrays.asList<Int>(R.string.title2, R.string.title3, R.string.title4, R.string.title5, R.string.title6, R.string.title7, R.string.title8, R.string.title9, R.string.title10))

        listImages = ArrayList(Arrays.asList<Int>(R.drawable.bike, R.drawable.castle, R.drawable.cow,
                R.drawable.hand, R.drawable.bug, R.drawable.notes,
                R.drawable.octopus, R.drawable.pencils, R.drawable.apples))

        listTxt1 = ArrayList(Arrays.asList<Int>(R.string.txt12, R.string.txt13, R.string.txt14, R.string.txt15, R.string.txt16, R.string.txt17, R.string.txt18, R.string.txt19, R.string.txt110))

        listTxt2 = ArrayList(Arrays.asList<Int>(R.string.txt22, R.string.txt23, R.string.txt24, R.string.txt25, R.string.txt26, R.string.txt27, R.string.txt28, R.string.txt29, R.string.txt210))

        listTimes = ArrayList(Arrays.asList<Int>(R.string.time1, R.string.time2, R.string.time3, R.string.time4, R.string.time5, R.string.time6, R.string.time7, R.string.time8, R.string.time9, R.string.time10))


        for (i in 0..8) {

            val LLV1 = LinearLayout(activity)
            val layoutParamsLLV1 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParamsLLV1.gravity= Gravity.CENTER_HORIZONTAL;
            LLV1.setLayoutParams(layoutParamsLLV1)
            LLV1.setGravity(Gravity.CENTER)
            LLV1.orientation = LinearLayout.VERTICAL

            val title = TextView(activity)
            val layoutParamsTitle = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            title.setLayoutParams(layoutParamsTitle)
            title.setText(listTitle!!.get(i))
            title.setTextColor(0xff000000)
            title.setTypeface(title.getTypeface(), Typeface.BOLD);
            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 33f)
            title.setGravity(Gravity.CENTER_HORIZONTAL)
            LLV1.addView(title)

            val LLH2 = LinearLayout(activity)
            val layoutParamsLLH2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsLLH2.gravity= Gravity.CENTER_HORIZONTAL;
            LLH2.setLayoutParams(layoutParamsLLH2)
            LLH2.setGravity(Gravity.CENTER_HORIZONTAL)
            LLH2.orientation = LinearLayout.HORIZONTAL

            val LLH3 = LinearLayout(activity)
            val layoutParamsLLH3 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsLLH3.gravity= Gravity.CENTER_HORIZONTAL;
            LLH3.setLayoutParams(layoutParamsLLH3)
            LLH3.setGravity(Gravity.CENTER_HORIZONTAL)
            LLH3.orientation = LinearLayout.HORIZONTAL

            for (j in 0..(level-1)) {
                val image = ImageView(activity)
                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.width = 100.dpToPixels(context)
                params.height = 80.dpToPixels(context)
                image.setLayoutParams(params)
                image.setImageResource(listImages!!.get(i))

                if(j <5) {
                    LLH2.addView(image)
                }
                else{
                    LLH3.addView(image)
                }
            }

            LLV1.addView(LLH2)
            LLV1.addView(LLH3)

            val extendedEquation = TextView(activity)
            val layoutParamsExtendedEquation = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsExtendedEquation.setMargins(10.dpToPixels(context),20.dpToPixels(context),10.dpToPixels(context),20.dpToPixels(context))
            extendedEquation.setLayoutParams(layoutParamsExtendedEquation)

            var str = ""+ (i+2) + " x " + level + " = "

            if(level > 1){
                str += (i+2)
                for (j in 0..(level-2)) {
                    str += " + " + (i+2)
                }
                str += " = "
            }

            str += (i+2)*level

            extendedEquation.setText(str)
            extendedEquation.setTextColor(0xffb7102f)
            extendedEquation.setTypeface(extendedEquation.getTypeface(), Typeface.BOLD);
            extendedEquation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            extendedEquation.setGravity(Gravity.CENTER_HORIZONTAL)
            LLV1.addView(extendedEquation)

            val LLH4 = LinearLayout(activity)
            val layoutParamsLLH4 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsLLH4.gravity= Gravity.CENTER_HORIZONTAL;
            LLH4.setLayoutParams(layoutParamsLLH4)
            LLH4.setGravity(Gravity.CENTER_HORIZONTAL)
            LLH4.orientation = LinearLayout.HORIZONTAL

            val txt1 = TextView(activity)
            val layoutParamsTxt1 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txt1.setLayoutParams(layoutParamsTxt1)
            txt1.setText(listTxt1!!.get(i))
            txt1.setTextColor(0xff000000)
            txt1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH4.addView(txt1)

            val multiplier1 = TextView(activity)
            val layoutParamsMultiplier1 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            multiplier1.setLayoutParams(layoutParamsMultiplier1)
            multiplier1.setText(" " + (i + 2).toString())
            multiplier1.setTextColor(0xffb7102f)
            multiplier1.setTypeface(title.getTypeface(), Typeface.BOLD);
            multiplier1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH4.addView(multiplier1)
            LLV1.addView(LLH4)

            val LLH5 = LinearLayout(activity)
            val layoutParamsLLH5 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsLLH5.gravity= Gravity.CENTER_HORIZONTAL;
            LLH5.setLayoutParams(layoutParamsLLH5)
            LLH5.setGravity(Gravity.CENTER_HORIZONTAL)
            LLH5.orientation = LinearLayout.HORIZONTAL

            val txt2 = TextView(activity)
            val layoutParamsTxt2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txt2.setLayoutParams(layoutParamsTxt2)
            txt2.setText(listTxt2!!.get(i))
            txt2.setTextColor(0xff000000)
            txt2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH5.addView(txt2)

            val multiplier2 = TextView(activity)
            val layoutParamsMultiplier2 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            multiplier2.setLayoutParams(layoutParamsMultiplier2)
            multiplier2.setText(" " + (level).toString())
            multiplier2.setTextColor(0xffb7102f)
            multiplier2.setTypeface(title.getTypeface(), Typeface.BOLD);
            multiplier2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH5.addView(multiplier2)
            LLV1.addView(LLH5)

            val LLH6 = LinearLayout(activity)
            val layoutParamsLLH6 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParamsLLH5.gravity= Gravity.CENTER_HORIZONTAL;
            LLH6.setLayoutParams(layoutParamsLLH5)
            LLH6.setGravity(Gravity.CENTER_HORIZONTAL)
            LLH6.orientation = LinearLayout.HORIZONTAL

            val txt3 = TextView(activity)
            val layoutParamsTxt3 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txt3.setLayoutParams(layoutParamsTxt3)
            txt3.setText(R.string.thus_the_number)
            txt3.setTextColor(0xff000000)
            txt3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH6.addView(txt3)

            val multiplier11 = TextView(activity)
            val layoutParamsMultiplier11 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            multiplier11.setLayoutParams(layoutParamsMultiplier11)
            multiplier11.setText(" " + (i + 2).toString() + " ")
            multiplier11.setTextColor(0xffb7102f)
            multiplier11.setTypeface(title.getTypeface(), Typeface.BOLD);
            multiplier11.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH6.addView(multiplier11)

            val txt4 = TextView(activity)
            val layoutParamsTxt4 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txt4.setLayoutParams(layoutParamsTxt4)
            txt4.setText(R.string.should_be_taken)
            txt4.setTextColor(0xff000000)
            txt4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH6.addView(txt4)

            val multiplier21 = TextView(activity)
            val layoutParamsMultiplier21 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            multiplier21.setLayoutParams(layoutParamsMultiplier21)
            multiplier21.setText(" " + (level).toString() + " ")
            multiplier21.setTextColor(0xffb7102f)
            multiplier21.setTypeface(title.getTypeface(), Typeface.BOLD);
            multiplier21.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH6.addView(multiplier21)

            val times = TextView(activity)
            val layoutParamsTimes = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            times.setLayoutParams(layoutParamsTimes)
            times.setText(listTimes!!.get(level-1))
            times.setTextColor(0xff000000)
            times.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH6.addView(times)

            val txt5 = TextView(activity)
            val layoutParamsTxt5 = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            txt5.setLayoutParams(layoutParamsTxt5)
            txt5.setText(": ")
            txt5.setTextColor(0xff000000)
            txt5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            LLH6.addView(txt5)

            val shortEquation = TextView(activity)
            val layoutParamsShortEquation = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            shortEquation.setLayoutParams(layoutParamsShortEquation)

            var str1 = ""+ (i+2) + " x " + level + " = " + (i+2)*level

            shortEquation.setText(str1)
            shortEquation.setTextColor(0xffb7102f)
            shortEquation.setTypeface(extendedEquation.getTypeface(), Typeface.BOLD);
            shortEquation.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)
            shortEquation.setGravity(Gravity.CENTER_HORIZONTAL)
            LLH6.addView(shortEquation)

            LLV1.addView(LLH6)
            viewFlipper.addView(LLV1)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.prev_button -> viewFlipper.showPrevious()
            R.id.next_button -> viewFlipper.showNext()
            R.id.back_button2 -> activity?.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        MainActivity.instance.setLocale(MainActivity.curLanguage)

        newConfig.locale = Locale(MainActivity.curLanguage)
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics())
    }
}

private fun TextView.setTextColor(color: Long) = this.setTextColor(color.toInt())

private fun Int.dpToPixels(context: Context?):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context?.resources?.displayMetrics
).toInt()