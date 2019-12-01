package com.stevenpopovich.trying_to_be_funny.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.stevenpopovich.trying_to_be_funny.R
import com.stevenpopovich.trying_to_be_funny.SetService
import com.stevenpopovich.trying_to_be_funny.SetServiceLocalSavingImpl
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class RecorderFragment : Fragment() {

    private var mediaRecorder = MediaRecorder()
    private var isRecording: Boolean = false
    private var recordingPaused: Boolean = false

    private val dialogTag = "save_dialog_id"

    companion object {
        fun newInstance() = RecorderFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_start_recording.setOnClickListener {
            if  (context != null) {
                if (ContextCompat.checkSelfPermission(
                        activity!!.applicationContext,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        activity!!.applicationContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val permissions = arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    ActivityCompat.requestPermissions(activity!!, permissions, 0)
                } else {
                    startRecording()
                }
            }
        }

        button_stop_recording.setOnClickListener {
            stopRecording()
        }

        button_pause_recording.setOnClickListener {
            pauseRecording()
        }

        replay_first_recording.setOnClickListener {
            val newService = SetServiceLocalSavingImpl(context!!)
            newService
                .getAllSets()
                .observeForever {
                    val set = it.random()
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource("${context!!.externalCacheDir?.absolutePath}/${set.recordingId}")
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private fun startRecording() {
        resetRecorder()
        mediaRecorder.start()
        isRecording = true
    }

    private fun stopRecording() {
        if (isRecording) {
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false

            val ft = fragmentManager!!.beginTransaction()
            val prev = fragmentManager!!.findFragmentByTag(dialogTag)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val dialogFragment = OnFinishRecordingFragment()
            dialogFragment.show(fragmentManager!!, dialogTag)
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        if (isRecording) {
            if (!recordingPaused) {
                mediaRecorder.pause()
                recordingPaused = true
                button_pause_recording.text = "Resume"
            } else {
                resumeRecording()
            }
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        mediaRecorder.resume()
        button_pause_recording.text = "Pause"
        recordingPaused = false
    }

    private fun resetRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        val randomRecordingPath = UUID.randomUUID().toString() + ".m4a"
        SetService.setRecordingId = randomRecordingPath
        mediaRecorder.setOutputFile(
            "${context!!.externalCacheDir?.absolutePath}/${randomRecordingPath}"
        )
        mediaRecorder.prepare()
    }
}
