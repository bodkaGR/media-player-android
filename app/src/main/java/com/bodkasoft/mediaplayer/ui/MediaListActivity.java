package com.bodkasoft.mediaplayer.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bodkasoft.mediaplayer.databinding.ActivityMediaListBinding;
import com.bodkasoft.mediaplayer.item.MediaItem;
import com.bodkasoft.mediaplayer.reader.MediaItemLoader;
import com.bodkasoft.mediaplayer.reader.UrisReader;
import com.bodkasoft.mediaplayer.ui.adapter.MediaAdapter;
import com.bodkasoft.mediaplayer.utils.MediaType;

import java.util.ArrayList;
import java.util.List;

public class MediaListActivity extends AppCompatActivity {
    private ActivityMediaListBinding binding;
    private  MediaAdapter adapter;
    private List<MediaItem> items = new ArrayList<>();
    private static final int IDM_PICK_FROM_STORAGE = 1002;
    private MediaType selectedMediaType = MediaType.MUSIC;
    private MediaItemLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMediaListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loader = new MediaItemLoader(new UrisReader(this));

        items.addAll(loader.loadAllMedia());

        adapter = new MediaAdapter(items, item -> {
            Intent intent = new Intent(this, item.getMediaType().getTargetActivity());
            intent.putExtra("MEDIA_URI", item.getUri());
            startActivity(intent);
        });

        binding.mediaList.setAdapter(adapter);
    }

    private void showMediaTypeChooser() {
        String[] types = {"Аудіо", "Відео", "Завантажити з Інтернету"};
        new AlertDialog.Builder(this)
                .setTitle("Оберіть тип файлу")
                .setItems(types, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            selectedMediaType = MediaType.MUSIC;
                            loadMediaByType(selectedMediaType);
                            break;
                        case 1:
                            selectedMediaType = MediaType.VIDEO;
                            loadMediaByType(selectedMediaType);
                            break;
                        case 2:
                            showInternetUrlInput();
                            break;
                    }
                })
                .show();
    }

    private void showInternetUrlInput() {
        EditText input = new EditText(this);
        input.setHint("Enter media file URL");

        new AlertDialog.Builder(this)
                .setTitle("Файл з інтернету")
                .setView(input)
                .setPositiveButton("Open", ((dialog, which) -> {
                    String url = input.getText().toString().trim();
                    if (!url.isEmpty()) {
                        openInternetMedia(url);
                    }else {
                        Toast.makeText(this, "URL must not be empty", Toast.LENGTH_SHORT).show();
                    }
                }))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openInternetMedia(String url) {
        MediaType mediaType = MediaType.fromMediaName(url);

        Intent intent = new Intent(this, mediaType.getTargetActivity());
        intent.putExtra("MEDIA_URI", Uri.parse(url).toString());
        startActivity(intent);
    }

    private void loadMediaByType(MediaType type) {
        items.clear();
        items.addAll(loader.loadMediaByType(type));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_PICK_FROM_STORAGE, 1, "Type")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case IDM_PICK_FROM_STORAGE:
                showMediaTypeChooser();
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}