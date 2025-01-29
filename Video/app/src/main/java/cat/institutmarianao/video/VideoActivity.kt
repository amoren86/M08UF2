package cat.institutmarianao.video

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class VideoActivity : AppCompatActivity() {

    companion object {
        const val VIDEO_FILE = "do_i_wanna_know.mp4"
    }

    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController
    private lateinit var bShowControls: Button

    private val requestManageStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
            loadVideo()
        } else {
            Toast.makeText(this, "The app cannot run without this permission", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private val requestReadStoragePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadVideo()
        } else {
            Toast.makeText(this, "The app cannot run without this permission", Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.video_view)
        bShowControls = findViewById(R.id.b_show_controls)

        mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        videoView.requestFocus()
        mediaController.setMediaPlayer(videoView)

        checkPermissions()

        bShowControls.setOnClickListener {
            mediaController.show()
        }
    }

    /**
     * Verify and ask for permissions depending on Android version
     */
    private fun checkPermissions() {
        // Android 11+ (API 30 and on)
        if (!Environment.isExternalStorageManager()) {
            requestManageStoragePermission()
        } else {
            loadVideo()
        }
    }


    private fun requestManageStoragePermission() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
            data = Uri.parse("package:$packageName")
        }
        requestManageStoragePermissionLauncher.launch(intent)
    }

    private fun loadVideo() {
        val moviesDir =
            File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_MOVIES)
        val videoFile = File(moviesDir, VIDEO_FILE)

        if (videoFile.exists()) {
            videoView.setVideoPath(videoFile.absolutePath)
        } else {
            Toast.makeText(this, "Video file not found", Toast.LENGTH_SHORT).show()
        }
    }
}
