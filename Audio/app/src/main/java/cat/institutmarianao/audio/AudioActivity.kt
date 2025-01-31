package cat.institutmarianao.audio


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.widget.Button
import android.widget.TextView

class AudioActivity : AppCompatActivity() {
    private lateinit var tvStatus: TextView
    private lateinit var bPlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        tvStatus = findViewById(R.id.tv_status)
        bPlay = findViewById(R.id.b_play)

        val player = MediaPlayer.create(this, R.raw.letitbe)

        bPlay.setOnClickListener {
            if (player.isPlaying) {
                player.stop()
                player.prepare()
            }
            player.start()
            tvStatus.text = "Playing..."
            player.setOnCompletionListener {
                tvStatus.text = "End of play"
            }
        }
    }
}
