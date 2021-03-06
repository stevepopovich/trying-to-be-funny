package com.stevenpopovich.trying_to_be_funny.ui.main

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.stevenpopovich.trying_to_be_funny.*
import com.stevenpopovich.trying_to_be_funny.ui.main.save_dialog_screens.OnFinishRecordingContainerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class RecorderFragment : Fragment() {

    private var mediaRecorder = MediaRecorder()
    private var isRecording: Boolean = false
    private var recordingPaused: Boolean = false

    private val dialogTag = "save_dialog_id"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNeededRecordingPermissions()

        button_start_recording.setOnClickListener {
            startRecording()
        }

        button_stop_recording.setOnClickListener {
            stopRecording()
        }

        showPauseButtonAboveVersionCodeN()

        //TODO temporary for testing
        replay_first_recording.setOnClickListener {
            val newService = SetServiceLocalSavingImpl(SetServiceLocalSavingImpl.getStandardDatabase(context!!))
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
        if (hasAllPermissions(context!!, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            resetRecorder()
            mediaRecorder.start()
            isRecording = true
            button_pause_recording.isEnabled = true
            button_stop_recording.isEnabled = true
            button_start_recording.isEnabled = false
        } else
            showYouDontHavePermissionsDialog()
    }

    private fun stopRecording() {
        if (isRecording) {
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false

            showSaveProcessDialog()

            button_start_recording.isEnabled = true
            button_pause_recording.isEnabled = false
            button_stop_recording.isEnabled = false
        }
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun togglePause() {
        if (isRecording) {
            if (!recordingPaused) {
                pauseRecording()
            } else {
                resumeRecording()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        mediaRecorder.resume()
        button_pause_recording.text = getString(R.string.pause)
        recordingPaused = false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        mediaRecorder.pause()
        recordingPaused = true
        button_pause_recording.text = getString(R.string.resume)
    }

    private fun getNeededRecordingPermissions() {
        askForNotGrantedPermissions(context!!, activity!!,
            listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun showSaveProcessDialog() {
        val dialogFragment = OnFinishRecordingContainerFragment()
        dialogFragment.setStyle(androidx.fragment.app.DialogFragment.STYLE_NORMAL, R.style.AppTheme)

        val transaction = fragmentManager!!
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_up_animation, R.anim.slide_down_animation, R.anim.slide_up_animation, R.anim.slide_down_animation)
            .add(R.id.container, dialogFragment, dialogTag)
            .addToBackStack(null)

        transaction.commit()
    }

    private fun showYouDontHavePermissionsDialog() {
        AlertDialog.Builder(activity!!)
            .setTitle(R.string.dont_have_permissions_to_record_and_save_title)
            .setMessage(R.string.dont_have_permissions_to_record_and_save_body)
            .setPositiveButton(R.string.okay) { _, _ ->
                getNeededRecordingPermissions()
            }
            .show()
    }

    private fun showPauseButtonAboveVersionCodeN() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            button_pause_recording.visibility = View.VISIBLE
            button_pause_recording.setOnClickListener {
                togglePause()
            }
        }
    }
}
