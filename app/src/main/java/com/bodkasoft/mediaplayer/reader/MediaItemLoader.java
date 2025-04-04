package com.bodkasoft.mediaplayer.reader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Pair;

import com.bodkasoft.mediaplayer.item.MediaItem;
import com.bodkasoft.mediaplayer.utils.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MediaItemLoader {
    private final Context context;
    private final UrisReader reader;

    public MediaItemLoader(Context context, UrisReader reader) {
        this.context = context;
        this.reader = reader;
    }

    public List<MediaItem> loadAllMedia() {
        List<MediaItem> result = new ArrayList<>();
        for (Pair<Uri, String[]> source: getMediaSources()) {
            result.addAll(reader.getExternalMediaItems(source.first, source.second));
        }
        result.addAll(reader.getInternalMediaItems());
        return result;
    }

    public List<MediaItem> loadMediaByType(MediaType type) {
        List<MediaItem> result = new ArrayList<>();
        Uri mediaUri = getMediaUriByType(type);

        result.addAll(reader.getExternalMediaItems(
                mediaUri,
                new String[]{MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DISPLAY_NAME}
        ));

        result.addAll(reader.getInternalMediaItems().stream()
                .filter(mediaItem -> MediaType.fromFileName(mediaItem.getName()).equals(type))
                .collect(Collectors.toList()
        ));

        return result;
    }

    private static Uri getMediaUriByType(MediaType type) {
        return (type == MediaType.MUSIC)
                ? MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                : MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }

    private List<Pair<Uri, String[]>> getMediaSources(){
        return Arrays.asList(
                new Pair<>(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME}
                ),
                new Pair<>(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME}
                )
        );
    }
}
