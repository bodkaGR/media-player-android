package com.bodkasoft.mediaplayer.viewmodel;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.bodkasoft.mediaplayer.R;

import java.io.IOException;

public class PlayerViewModel extends AndroidViewModel {
    private MediaPlayer mediaPlayer;

    public PlayerViewModel(Application application) {
        super(application);
        mediaPlayer = MediaPlayer.create(application, R.raw.ai_molodets);
    }

    public void playAudio() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pauseAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopAudio() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
