package com.bodkasoft.mediaplayer.utils;

import android.app.Activity;

import com.bodkasoft.mediaplayer.ui.AudioActivity;
import com.bodkasoft.mediaplayer.ui.VideoActivity;

import java.util.Arrays;

public enum MediaType {
    MUSIC(".mp3") {
        @Override
        public Class<? extends Activity> getTargetActivity() {
            return AudioActivity.class;
        }
    },
    VIDEO(".mp4") {
        @Override
        public Class<? extends Activity> getTargetActivity() {
            return VideoActivity.class;
        }
    };

    private final String extension;

    MediaType(String extension) {
        this.extension = extension;
    }

    public abstract Class<? extends Activity> getTargetActivity();

    public static MediaType fromMediaName(String mediaName) {
        return Arrays.stream(values())
                .filter(mediaType -> mediaName.toLowerCase().endsWith(mediaType.extension))
                .findFirst()
                .orElse(null);
    }
}
