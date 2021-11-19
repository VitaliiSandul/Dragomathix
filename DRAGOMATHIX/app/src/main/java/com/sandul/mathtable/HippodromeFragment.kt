package com.sandul.mathtable

import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.util.*

class HippodromeFragment : Fragment(), View.OnClickListener{

    private lateinit var btnVar1: ImageButton
    private lateinit var btnVar2: ImageButton
    private lateinit var btnVar3: ImageButton
    private lateinit var btnVar4: ImageButton
    private lateinit var closeButton: ImageButton
    private lateinit var player: ImageView
    private lateinit var pc: ImageView
    private lateinit var txtVar: Array<TextView>
    private lateinit var imgCorIncor: Array<ImageView>
    private lateinit var mathExpressionTxt: TextView
    private lateinit var questionMarkTxt: TextView
    private lateinit var txtScorePlayer: TextView
    private lateinit var txtScorePc: TextView
    private var questionNum: Int = 15
    private var scorePlayer: Int = 0
    private var scorePc: Int = 0
    private var display: Display? = null
    private var size: Point? = null
    private var width = 0
    private var delta = 0f
    private var playerX = 0f
    private var pcX = 0f
    private var playerStartX = 0f
    private var pcStartX = 0f

    private val tableViewModel: TableViewModel by lazy {
        ViewModelProviders.of(this).get(TableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (container == null)
            return null

        val v = inflater.inflate(R.layout.fragment_hippodrome, container, false)
        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        findDisplayWidthHeight()

        initBtns(v)
        clickBtns()
        setCharacter(MainActivity.curGender)
        tableViewModel.shuffleQuestionBank()
        fillQuestionVars()

        return v
    }

    private fun findDisplayWidthHeight() {
        display = activity?.getWindowManager()?.getDefaultDisplay()
        size = Point()
        display?.getSize(size)

        var wd = size!!.x
        var ht = size!!.y

        width = wd
        if (wd < ht) width = ht

        delta = (width * 0.9 / questionNum).toFloat()
    }

    override fun onClick(v: View?) {
        playerStartX = player.x
        pcStartX = pc.x

        var selectedAnswer: String = ""
        val random = Random()
        val pcWin = random.nextInt(15) + 1
        Log.d("Random pcWin", pcWin.toString())

        when (v?.id) {
            R.id.btn_var1 -> {
                selectedAnswer = tableViewModel.currentQuestionVar1.toString()
            }
            R.id.btn_var2 -> {
                selectedAnswer = tableViewModel.currentQuestionVar2.toString()
            }
            R.id.btn_var3 -> {
                selectedAnswer = tableViewModel.currentQuestionVar3.toString()
            }
            R.id.btn_var4 -> {
                selectedAnswer = tableViewModel.currentQuestionVar4.toString()
            }
            R.id.close_button->{
                activity?.onBackPressed()
            }
        }

        if(selectedAnswer.equals(tableViewModel.currentQuestionAnswer.toString())){
            scorePlayer++
            playerX += delta

            var newList = MainActivity.curProgress.toMutableList()
            if(newList.get(tableViewModel.currentQuestionCategory) < 100) {
                newList.set(
                        tableViewModel.currentQuestionCategory,
                        newList.get(tableViewModel.currentQuestionCategory) + 1
                )
                MainActivity.curProgress = newList.toList()
                MainActivity.instance.updateProgress()
                MainActivity.instance.updatePlayerSettingsFun()
            }

            animatePlayer()
        }

        if(pcWin in 0..13){
            scorePc++
            pcX += delta
            animatePc()
        }

        drawCorIncorImgs()

        questionMarkTxt.text = tableViewModel.currentQuestionAnswer.toString()
        unclickBtns()

        for (img in imgCorIncor) {
            img.visibility = View.VISIBLE
        }

        refreshScore()

        Utils.runOnUiThread {
            val handler = Handler()
            handler.postDelayed({
                tableViewModel.moveToNext()
                fillQuestionVars()
                clickBtns()
            }, 2000)
        }

        if(scorePc == 15 || scorePlayer == 15){

            var msg:String = ""

            if (scorePlayer > scorePc){
                msg = getString(R.string.you_win)
            }
            else if(scorePlayer < scorePc){
                msg = getString(R.string.you_lose)
            }
            else{
                msg = getString(R.string.draw_in_the_race)
            }

            val d = ResultDialogFragment()
            val b = Bundle()
            b.putString("KEY2", msg)
            d.arguments = b
            d.setTargetFragment(this@HippodromeFragment, 1)
            fragmentManager?.let { d.show(it, "ChooseTestDialogFragment") }
        }
    }

    private fun clickBtns(){
        btnVar1.setOnClickListener(this)
        btnVar2.setOnClickListener(this)
        btnVar3.setOnClickListener(this)
        btnVar4.setOnClickListener(this)
        closeButton.setOnClickListener(this)
    }

    private fun unclickBtns(){
        btnVar1.setOnClickListener(null)
        btnVar2.setOnClickListener(null)
        btnVar3.setOnClickListener(null)
        btnVar4.setOnClickListener(null)
        closeButton.setOnClickListener(null)
    }

    private fun initBtns(view: View?) {
        btnVar1 = view?.findViewById<ImageButton>(R.id.btn_var1)!!
        btnVar2 = view?.findViewById<ImageButton>(R.id.btn_var2)!!
        btnVar3 = view?.findViewById<ImageButton>(R.id.btn_var3)!!
        btnVar4 = view?.findViewById<ImageButton>(R.id.btn_var4)!!
        closeButton = view?.findViewById<ImageButton>(R.id.close_button)!!
        txtScorePlayer = view?.findViewById<TextView>(R.id.txt_score_player)!!
        txtScorePc = view?.findViewById<TextView>(R.id.txt_score_pc)!!
        mathExpressionTxt = view?.findViewById<TextView>(R.id.math_expression)!!
        questionMarkTxt = view?.findViewById<TextView>(R.id.question_mark)!!

        txtVar = arrayOf<TextView>(view.findViewById<TextView>(R.id.txt_var1), view.findViewById<TextView>(R.id.txt_var2),
                view.findViewById<TextView>(R.id.txt_var3), view.findViewById<TextView>(R.id.txt_var4))

        imgCorIncor = arrayOf<ImageView>(view.findViewById<ImageView>(R.id.imgCorIncor1), view.findViewById<ImageView>(R.id.imgCorIncor2),
                view.findViewById<ImageView>(R.id.imgCorIncor3), view.findViewById<ImageView>(R.id.imgCorIncor4))

        player = view?.findViewById<ImageView>(R.id.player)!!
        pc = view?.findViewById<ImageView>(R.id.pc)!!
    }

    private fun fillQuestionVars(){

        for (img in imgCorIncor) {
            img.visibility = View.GONE
        }
        mathExpressionTxt.text = tableViewModel.currentQuestionText
        questionMarkTxt.text = " ? "
        txtVar.get(0).text = tableViewModel.currentQuestionVar1.toString()
        txtVar.get(1).text = tableViewModel.currentQuestionVar2.toString()
        txtVar.get(2).text = tableViewModel.currentQuestionVar3.toString()
        txtVar.get(3).text = tableViewModel.currentQuestionVar4.toString()
    }

    private fun drawCorIncorImgs() {
        var index = 0
        for (img in imgCorIncor) {
            if(txtVar.get(index).text.equals(tableViewModel.currentQuestionAnswer.toString())){
                img.setImageResource(R.drawable.correct_answer)
            }
            else{
                img.setImageResource(R.drawable.incorrect_answer)
            }
            index++
        }
    }

    private fun refreshScore() {
        txtScorePlayer.text = scorePlayer.toString()
        txtScorePc.text = scorePc.toString()
    }

    private fun animatePlayer() {
        val slideUp: Animation = TranslateAnimation(0f, -(playerStartX - playerX), 0f, 0f)
        slideUp.duration = 1000
        slideUp.fillAfter = true
        val animSet = AnimationSet(true)
        animSet.isFillEnabled = true
        animSet.addAnimation(slideUp)
        player.startAnimation(animSet)
        playerStartX = playerX
        player.x = playerStartX
    }

    private fun animatePc() {
        val slideUp: Animation = TranslateAnimation(0f, -(pcStartX - pcX), 0f, 0f)
        slideUp.duration = 1000
        slideUp.fillAfter = true
        val animSet = AnimationSet(true)
        animSet.isFillEnabled = true
        animSet.addAnimation(slideUp)
        pc.startAnimation(animSet)
        pcStartX = pcX
        pc.x = pcStartX
    }

    private fun setCharacter(gender: String) {
        if(gender.equals("boy")){
            player.setImageResource(R.drawable.horse_boy)
        }
        else{
            player.setImageResource(R.drawable.horse_girl)
        }
    }
}