package com.bodkasoft.mediaplayer.utils;

import java.util.Arrays;

public enum MediaType {
    MUSIC(".mp3"),
    VIDEO(".mp4");

    private final String extension;

    MediaType(String extension) {
        this.extension = extension;
    }

    public static MediaType fromFileName(String fileName) {
        return Arrays.stream(values())
                .filter(mediaType -> fileName.toLowerCase().endsWith(mediaType.extension))
                .findFirst()
                .orElse(null);
    }
}
