package com.stevenpopovich.trying_to_be_funny.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.stevenpopovich.trying_to_be_funny.R
import kotlinx.android.synthetic.main.main_fragment.*

class RecorderFragment : Fragment() {

    private lateinit var mediaRecorder: MediaRecorder
    private var output: String = ""
    private var isRecording: Boolean = false
    private var recordingPaused: Boolean = false

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        output = Environment.getExternalStorageDirectory().absolutePath + "/recording.mp3"
        mediaRecorder = MediaRecorder()

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(output)

        mediaRecorder.prepare()

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    private fun startRecording() {
        stopRecording()
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(output)
        mediaRecorder.prepare()
        mediaRecorder.start()
        isRecording = true
    }

    private fun stopRecording() {
        if (isRecording) {
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false

            val ft = fragmentManager!!.beginTransaction()
            val prev = fragmentManager!!.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val dialogFragment = OnFinishRecordingFragment()
            dialogFragment.show(fragmentManager!!, "dialog")
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
}
