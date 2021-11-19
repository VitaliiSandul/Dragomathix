package com.sandul.mathtable

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.onesignal.OneSignal
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete
import java.util.*

const val ONESIGNAL_APP_ID = "ba466cea-e856-4358-8d6a-2931519fbdb9"

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var mAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "HippodromeFragment"
    
    private lateinit var mainPage: ConstraintLayout
    private lateinit var reset: ImageView
    private lateinit var exit: ImageView
    private lateinit var soundon: ImageView
    private lateinit var soundoff: ImageView
    private lateinit var language: ImageView
    private lateinit var engLang: ImageView
    private lateinit var ukrLang: ImageView
    private lateinit var rusLang: ImageView
    private lateinit var chooseCharacter: ImageView
    private lateinit var characterBoy: ImageView
    private lateinit var characterGirl: ImageView
    private lateinit var characterBig: ImageView
    private lateinit var progressbarMain: ProgressBar
    private lateinit var txtProgressbarMain:TextView
    private lateinit var btnTables: ImageButton
    private lateinit var btnLessons: ImageButton
    private lateinit var btnTest: ImageButton
    private lateinit var btnGames: ImageButton
    private lateinit var playerSettingsDao: PlayerSettingsDao

    companion object {
        lateinit var curLanguage: String
        lateinit var curGender: String
        lateinit var curProgress: List<Int>
        var curIsSoundOn: Boolean = false
        lateinit var curDate: Date
        lateinit var ps: PlayerSettings
        lateinit var instance: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "playset.db").build()
        playerSettingsDao = db.playerSettingsDao()

        doAsync {
            if(playerSettingsDao.getAll().size == 0){
                GlobalScope.launch {
                    ps = PlayerSettings(0, "en", "boy", Date(), listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
                    db.playerSettingsDao().insert(ps)
                }
            }
            else{
                ps = playerSettingsDao.getAll()[0]
            }

            curLanguage = ps.language!!
            curGender = ps.gender!!
            curDate = ps.date!!
            curProgress = ps.progress!!
            setLocale(curLanguage)

            onComplete{
                initComponents()
                click()
                fillElements()
                checkDaysWithoutTraining()
            }
        }

        setContentView(R.layout.activity_main)

        val serviceClass = MusicService::class.java
        val intent = Intent(this, serviceClass)
        if (!isServiceRunning(serviceClass)) {
            startService(intent)
        }

        hideNavBar()
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        instance = this

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        InterstitialAd.load(this,"ca-app-pub-5977337362697128/4842301634", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setLocale(curLanguage)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavBar()
    }

    private fun hideNavBar() {
        val decorator = window.decorView
        val uiOption = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorator.systemUiVisibility = uiOption
    }

    private fun initComponents(){
        mainPage = findViewById(R.id.main_page)
        reset = findViewById(R.id.reset)
        exit = findViewById(R.id.exit)
        soundon = findViewById(R.id.soundon)
        soundoff = findViewById(R.id.soundoff)
        language = findViewById(R.id.language)
        engLang = findViewById(R.id.eng_lang)
        ukrLang = findViewById(R.id.ukr_lang)
        rusLang = findViewById(R.id.rus_lang)
        chooseCharacter = findViewById(R.id.choose_character)
        characterBoy = findViewById(R.id.character_boy)
        characterGirl = findViewById(R.id.character_girl)
        characterBig = findViewById(R.id.character_big)
        progressbarMain = findViewById(R.id.progressbar_main)
        txtProgressbarMain = findViewById(R.id.txt_progressbar_main)
        btnTables = findViewById(R.id.btn_tables)
        btnLessons = findViewById(R.id.btn_lessons)
        btnTest = findViewById(R.id.btn_test)
        btnGames = findViewById(R.id.btn_games)
    }

    private fun fillElements(){
        val lang = curLanguage

        Log.d("lang", lang)
        when (lang) {
            "en" -> language.setBackgroundResource(R.drawable.eng_lang)
            "uk" -> language.setBackgroundResource(R.drawable.ukr_lang)
            "ru" -> language.setBackgroundResource(R.drawable.rus_lang)
        }

        when (curGender) {
            "boy" -> characterBoyFun()
            "girl" -> characterGirlFun()
        }

        when (curIsSoundOn) {
            true -> {
                soundon.visibility = View.VISIBLE
                soundoff.visibility = View.GONE
            }
            false -> {
                soundon.visibility = View.GONE
                soundoff.visibility = View.VISIBLE
            }
        }
        updateProgress()
    }

    private fun click(){
        exit.setOnClickListener(this)
        reset.setOnClickListener(this)
        soundon.setOnClickListener(this)
        soundoff.setOnClickListener(this)
        language.setOnClickListener(this)
        engLang.setOnClickListener(this)
        ukrLang.setOnClickListener(this)
        rusLang.setOnClickListener(this)
        chooseCharacter.setOnClickListener(this)
        characterBoy.setOnClickListener(this)
        characterGirl.setOnClickListener(this)
        btnTables.setOnClickListener(this)
        btnLessons.setOnClickListener(this)
        btnTest.setOnClickListener(this)
        btnGames.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.language -> languageFun()
            R.id.eng_lang -> setLanguage("en")
            R.id.ukr_lang -> setLanguage("uk")
            R.id.rus_lang -> setLanguage("ru")

            R.id.choose_character -> chooseCharacterFun()
            R.id.character_boy -> characterBoyFun()
            R.id.character_girl -> characterGirlFun()

            R.id.exit -> exitFun()
            R.id.reset -> resetFun()
            R.id.soundon -> soundOnFun()
            R.id.soundoff -> soundOffFun()

            R.id.btn_tables -> btnTablesFun()
            R.id.btn_lessons -> btnLessonsFun()
            R.id.btn_test -> btnTestFun()
            R.id.btn_games -> btnGamesFun()
        }
    }

    private fun languageFun() {
        language.visibility = View.GONE
        engLang.visibility = View.VISIBLE
        ukrLang.visibility = View.VISIBLE
        rusLang.visibility = View.VISIBLE
    }

    private fun setLanguage(lang: String){
        curLanguage = lang
        updatePlayerSettingsFun()
        when (lang) {
            "en" -> language.setBackgroundResource(R.drawable.eng_lang)
            "uk" -> language.setBackgroundResource(R.drawable.ukr_lang)
            "ru" -> language.setBackgroundResource(R.drawable.rus_lang)
        }

        language.visibility = View.VISIBLE
        engLang.visibility = View.GONE
        ukrLang.visibility = View.GONE
        rusLang.visibility = View.GONE

        setLocale(lang)
        recreate()
    }

    private fun btnGamesFun() {
        val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        ft.replace(R.id.container, HippodromeFragment(), "hippodrometag")
        .addToBackStack(null)
        .commit()

//        if (mInterstitialAd != null) {
//            mInterstitialAd?.show(this)
//        } else {
//            Log.d("TAG", "The interstitial ad wasn't ready yet.")
//        }
    }

    private fun btnTestFun() {
        val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        ft.replace(R.id.container, TestInputFragment(), "testtag")
        .addToBackStack(null)
        .commit()
    }

    private fun btnLessonsFun() {
        val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        ft.replace(R.id.container, LessonFragment(), "lessontag")
        .addToBackStack(null)
        .commit()
    }

    private fun btnTablesFun() {
        val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        ft.replace(R.id.container, TablesFragment(), "tablestag")
        .addToBackStack(null)
        .commit()
    }

    private fun chooseCharacterFun() {
        chooseCharacter.visibility = View.GONE
        characterBoy.visibility = View.VISIBLE
        characterGirl.visibility = View.VISIBLE
    }

    private fun characterGirlFun() {
        mainPage.setBackgroundResource(R.drawable.back_girl)
        characterBig.setBackgroundResource(R.drawable.dragon_girl_circle)
        chooseCharacter.setBackgroundResource(R.drawable.dragon_girl_circle_small)
        chooseCharacter.visibility = View.VISIBLE
        characterBoy.visibility = View.GONE
        characterGirl.visibility = View.GONE

        curGender="girl"
        updatePlayerSettingsFun()
    }

    private fun characterBoyFun() {
        mainPage.setBackgroundResource(R.drawable.back_boy)
        characterBig.setBackgroundResource(R.drawable.dragon_boy_circle)
        chooseCharacter.setBackgroundResource(R.drawable.dragon_boy_circle_small)
        chooseCharacter.visibility = View.VISIBLE
        characterBoy.visibility = View.GONE
        characterGirl.visibility = View.GONE

        curGender="boy"
        updatePlayerSettingsFun()
    }

    private fun soundOnFun() {
        soundon.visibility = View.GONE
        soundoff.visibility = View.VISIBLE

        curIsSoundOn = false

        if (isServiceRunning(MusicService::class.java)) {
            MusicService.soundOff()
        }
    }

    private fun soundOffFun() {
        soundon.visibility = View.VISIBLE
        soundoff.visibility = View.GONE

        curIsSoundOn = true

        if (isServiceRunning(MusicService::class.java)) {
            MusicService.soundOn()
        }
    }

    private fun resetFun() {
        val d = ResultDialogFragment()
        val b = Bundle()
        b.putString("KEY3", getString(R.string.do_you_want_to_reset_your_progress))
        d.arguments = b
        d.show(supportFragmentManager, "asc_dialog")
    }

    private fun exitFun() {
        val d = ResultDialogFragment()
        val b = Bundle()
        b.putString("KEY5", getString(R.string.do_you_want_exit))
        d.arguments = b
        d.show(supportFragmentManager, "asc_dialog")
    }

    fun updatePlayerSettingsFun() {
        doAsync {
            playerSettingsDao.update(PlayerSettings(0, curLanguage, curGender, Date(), curProgress))
        }
    }

    private fun calcProgress(): Int {
        var sum = 0
        curProgress.forEach{
            sum += it
        }
        return  sum/curProgress.size
    }

    private fun reduceProgress(dd: Long){
        var newProgress: List<Int>  = curProgress.map{ (it - dd*0.1).toInt() }.mapNotNull { if ( it < 0) 0 else it }.toList()
        curProgress = newProgress

        Log.d("curProgress", curProgress.toString())
    }

    private fun checkDaysWithoutTraining(){

        Log.d("curDate", curDate.toString())
        Log.d("Date", Date().toString())

        val diff: Long = Date().time - curDate.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        Log.d("diff seconds", seconds.toString())
        Log.d("diff minutes", minutes.toString())
        Log.d("diff hours", hours.toString())
        Log.d("diff days", days.toString())

        if(days > 5 && calcProgress() > 0){
//                if(minutes >= 3 && calcProgress() > 0){
            reduceProgress(days-5)
//                    val min = (minutes/3).toLong()
//                    reduceProgress(min)
            curDate = Date()
            updatePlayerSettingsFun()
            updateProgress()

            val d = ResultDialogFragment()
            val b = Bundle()
            b.putLong("KEY4", days)
//            b.putLong("KEY4", min)
            d.arguments = b
            d.show(getSupportFragmentManager().beginTransaction(), "")
        }
    }

    fun updateProgress(){
        progressbarMain.progress = calcProgress()
        txtProgressbarMain.text = calcProgress().toString() + " %"
    }

    fun setLocale(lang: String){
        val languageToLoad = lang
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        getResources().updateConfiguration(config, getResources().getDisplayMetrics())
    }

    override fun onResume() {
        super.onResume()

        Log.d("curIsSoundOn1", curIsSoundOn.toString())

        if(curIsSoundOn && isServiceRunning(MusicService::class.java)){
            Log.d("curIsSoundOn2", curIsSoundOn.toString())
            MusicService.soundOn()
        }
        else if(curIsSoundOn && !isServiceRunning(MusicService::class.java)){
            startService(Intent(this, MusicService::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        if (isServiceRunning(MusicService::class.java)) {
            MusicService.soundOff()
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        supportFragmentManager.popBackStack()
    }


}