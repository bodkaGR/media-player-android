package com.bodkasoft.mediaplayer.item;

public class MediaItem {
    private final String name;
    private final String uri;
    private final boolean isVideo;

    public MediaItem(String name, String uri, boolean isVideo) {
        this.name = name;
        this.uri = uri;
        this.isVideo = isVideo;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public boolean isVideo() {
        return isVideo;
    }
}
