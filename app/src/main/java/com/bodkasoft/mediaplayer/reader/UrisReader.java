package com.bodkasoft.mediaplayer.reader;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.bodkasoft.mediaplayer.item.MediaItem;
import com.bodkasoft.mediaplayer.utils.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UrisReader {
    private final Context context;

    public UrisReader(Context context) {
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

            if (name.toLowerCase().endsWith(".mp3")) {
                mediaItems.add(new MediaItem(name, uri.toString(), MediaType.MUSIC));
            } else if (name.toLowerCase().endsWith(".mp4")) {
                mediaItems.add(new MediaItem(name, uri.toString(), MediaType.VIDEO));
            }
        }
        return mediaItems;
    }

    public List<MediaItem> getAllAudioItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME
        };

        Cursor cursor = context.getContentResolver().query(
            uri, projection, null, null, null
        );

        if (cursor != null) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                String audioName = cursor.getString(nameColumn);

                mediaItems.add(new MediaItem(audioName, contentUri.toString(), MediaType.MUSIC));
            }
            cursor.close();
        }
        return mediaItems;
    }

    public List<MediaItem> getAllVideoItems() {
        List<MediaItem> mediaItems = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME
        };

        Cursor cursor = context.getContentResolver().query(
            uri, projection, null, null, MediaStore.Video.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            int idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                String audioName = cursor.getString(nameColumn);

                mediaItems.add(new MediaItem(audioName, contentUri.toString(), MediaType.VIDEO));
            }
            cursor.close();
        }
        return mediaItems;
    }
}
