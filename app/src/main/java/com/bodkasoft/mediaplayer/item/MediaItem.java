package com.bodkasoft.mediaplayer.item;

import com.bodkasoft.mediaplayer.utils.MediaType;

public class MediaItem {
    private final String name;
    private final String uri;
    private final MediaType mediaType;

    public MediaItem(String name, String uri, MediaType mediaType) {
        this.name = name;
        this.uri = uri;
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public MediaType directoryType() {
        return mediaType;
    }
}
