package jp.tomiyama.noir.spinnersample

import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    var selectedItems: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0, 0, 0)

    private val audioAttributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    private val mSoundResource = arrayOf(
        R.raw.c4,
        R.raw.d,
        R.raw.e,
        R.raw.f,
        R.raw.g,
        R.raw.a,
        R.raw.b,
        R.raw.c5
    )

    private var mSoundPool = SoundPool.Builder()
        .setAudioAttributes(audioAttributes)
        .setMaxStreams(mSoundResource.size)
        .build()

    private var mSoundID = arrayOfNulls<Int?>(mSoundResource.size)

    private var mTimer: Timer? = null

    private val mOnItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        // アイテムが選択されなかったとき
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        // アイテムが選択されたとき
        override fun onItemSelected(parent: AdapterView<*>, view: View, postion: Int, id: Long) {
            Log.d("position", postion.toString())

            when (parent.id) {
                R.id.firstSpinner -> {
                    selectedItems[0] = postion
                }
                R.id.secondSpinner -> {
                    selectedItems[1] = postion
                }
                R.id.thirdSpinner -> {
                    selectedItems[2] = postion
                }
                R.id.forthSpinner -> {
                    selectedItems[3] = postion
                }
                R.id.fifthSpinner -> {
                    selectedItems[4] = postion
                }
                R.id.sixthSpinner -> {
                    selectedItems[5] = postion
                }
                R.id.seventhSpinner -> {
                    selectedItems[6] = postion
                }
                R.id.eightSpinner -> {
                    selectedItems[7] = postion
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setSpinnerAdapter()

        val handler = Handler()
        var index: Int

        fab.setOnClickListener {
            if (mTimer == null) {
                mTimer = Timer(false)
                index = 0

                // 1.0秒おきに，サウンドを再生
                mTimer?.schedule(0, 1000) {
                    handler.post {
                        if (index < mSoundResource.size) {
                            mSoundID[selectedItems[index]]?.let { it1 ->
                                mSoundPool.play(it1, 1.0F, 1.0F, 0, 0, 1.0F)
                            }
                            index++
                        } else {
                            mTimer?.cancel()
                            mTimer = null
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        for (i in mSoundResource.indices) {
            mSoundID[i] = mSoundPool.load(applicationContext, mSoundResource[i], 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mSoundPool.release()
    }


    private fun setSpinnerAdapter() {
        val noteAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.noteList)
        )

        val spinners = arrayOf(
            firstSpinner,
            secondSpinner,
            thirdSpinner,
            forthSpinner,
            fifthSpinner,
            sixthSpinner,
            seventhSpinner,
            eightSpinner
        )

        for (s in spinners) {
            s.adapter = noteAdapter
            s.onItemSelectedListener = mOnItemSelectedListener
        }
    }
}
