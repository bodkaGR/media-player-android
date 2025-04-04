package com.bodkasoft.mediaplayer.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bodkasoft.mediaplayer.databinding.ActivityAudioBinding;

public class AudioActivity extends AppCompatActivity {
    private MediaPlayer player;
    private final Handler handler = new Handler();
    private ActivityAudioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAudioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String audioUri = getIntent().getStringExtra("MEDIA_URI");

        player = MediaPlayer.create(this, Uri.parse(audioUri));
        binding.seekBar.setMax(player.getDuration());
        setupListeners();
    }

    private void setupListeners() {
        binding.playButton.setOnClickListener(v -> {
            if (!player.isPlaying()) {
                player.start();
                updateSeekBar();
            }
        });

        binding.pauseButton.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
            }
        });

        binding.stopButton.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.stop();
                player.prepareAsync();
                binding.seekBar.setProgress(0);
            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // refactor: player not exist if activity is died
    private void updateSeekBar() {
        if (player != null) {
            binding.seekBar.setProgress(player.getCurrentPosition());
            if (player.isPlaying()) {
                handler.postDelayed(this::updateSeekBar, 500);
            }
        }
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}