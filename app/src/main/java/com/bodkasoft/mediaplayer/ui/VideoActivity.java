package com.bodkasoft.mediaplayer.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bodkasoft.mediaplayer.R;
import com.bodkasoft.mediaplayer.databinding.VideoActivityBinding;

public class VideoActivity extends AppCompatActivity {
    private MediaController mediaController;
    private VideoActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = VideoActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mediaController = new MediaController(this);

        setupListeners();

        String mediaUri = getIntent().getStringExtra("MEDIA_URI");

        playVideo(Uri.parse(mediaUri));
    }

    public void playVideo(Uri videoUri) {
        binding.videoView.setVideoURI(videoUri);
        binding.videoView.setMediaController(mediaController);
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.requestFocus();
        binding.videoView.start();
    }

    private void setupListeners() {
        binding.videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e("VideoView", "Error: " + what + ", Extra: " + extra);
            return true;
        });
    }

    @Override
    protected void onPause() {
        Log.v("MediaVideo", "onPause");
        super.onPause();
        binding.videoView.pause();
        binding.videoView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        Log.v("MediaVideo", "onResume");
        super.onResume();
        binding.videoView.resume();
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
