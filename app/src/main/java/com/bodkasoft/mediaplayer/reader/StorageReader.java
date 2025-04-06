package com.bodkasoft.mediaplayer.reader;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.bodkasoft.mediaplayer.item.MediaItem;
import com.bodkasoft.mediaplayer.utils.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StorageReader {
    private final Context context;

    public StorageReader(Context context) {
        this.context = context;
    }

    public List<MediaItem> getInternalMediaItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        File directory = context.getFilesDir();

        if (!directory.exists() || !directory.isDirectory()) {
            return mediaItems;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return mediaItems;
        }

        for (File file : files) {
            if (!file.isFile()) continue;

            String name = file.getName();
            Uri uri = Uri.fromFile(file);

            MediaType mediaType = MediaType.fromMediaName(name);
            if (mediaType != null) {
                mediaItems.add(new MediaItem(name, uri.toString(), mediaType));
            }
        }
        return mediaItems;
    }

    public List<MediaItem> getExternalMediaItems(Uri mediaUri, String[] projection) {
        List<MediaItem> mediaItems = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                mediaUri, projection, null, null, null
        );

        if (cursor != null) {
            int idColumn = cursor.getColumnIndex(projection[0]);
            int nameColumn = cursor.getColumnIndex(projection[1]);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                Uri contentUri = ContentUris.withAppendedId(mediaUri, id);

                mediaItems.add(new MediaItem(name, contentUri.toString(), MediaType.fromMediaName(name)));
            }
            cursor.close();
        }
        return mediaItems;
    }
}
