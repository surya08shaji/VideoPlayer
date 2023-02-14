package com.example.video

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.video.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
//    private var isLandScape: Boolean = false

    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    //    private val videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1"
    private val videoUrl =
        "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-webm-file.webm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        mediaPlayer = MediaPlayer().apply {
            setDataSource(
                applicationContext,
                Uri.parse(videoUrl)
            )
            prepareAsync()
            start()
        }

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.videoView.setVideoPath(videoUrl)

        binding.playBtn.setOnClickListener {

            if (mediaPlayer!!.isPlaying) {

                binding.videoView.pause()
                mediaPlayer!!.pause()

                binding.playBtn.setImageResource(R.drawable.play)

            } else {
                binding.playBtn.setImageResource(R.drawable.pause)

                binding.videoView.start()
                mediaPlayer!!.start()
                mediaPlayer!!.seekTo(mediaPlayer!!.currentPosition)

//                isFullScreen()
            }

            initializeSeekBar()
        }

        //View.OnClickListener imgButtonHandler = new View.OnClickListener() {
        //
        //    public void onClick(View v) {
        //
        //        imgButton.setBackgroundResource(R.drawable.ic_launcher);
        //
        //    }


        binding.fullScreen.setOnClickListener {

            val currentOrientation = resources.configuration.orientation

            requestedOrientation = if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                binding.fullScreen.setImageResource(R.drawable.fullscreen_exit)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            } else {
                binding.fullScreen.setImageResource(R.drawable.fullscreen)
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED


            }
        }

        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer!!.seekTo(progress * 1000)
                        binding.videoView.seekTo(progress * 1000)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )
    }

    private fun initializeSeekBar() {
        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer!!.currentPosition / 1000
            binding.seekBar.progress = binding.videoView.currentPosition / 1000

            val startTime = createTimeLabel(mediaPlayer!!.currentPosition)
            binding.startTime.text = startTime

            val endingTime = createTimeLabel(mediaPlayer!!.duration - mediaPlayer!!.currentPosition)
            binding.endingTime.text = endingTime

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

    }

    private fun createTimeLabel(time: Int): String {

        var timeLabel: String
        val min = time / 1000 / 60
        val sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }
}
