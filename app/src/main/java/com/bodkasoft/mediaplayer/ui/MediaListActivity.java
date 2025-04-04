package com.bodkasoft.mediaplayer.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.util.stream.Collectors;

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

        loader = new MediaItemLoader(this, new UrisReader(this));

        items.addAll(loader.loadAllMedia());

        adapter = new MediaAdapter(items, item -> {
            Intent intent = (item.directoryType().equals(MediaType.VIDEO))
                    ? new Intent(this, VideoActivity.class): new Intent(this, AudioActivity.class);
            intent.putExtra("MEDIA_URI", item.getUri());
            startActivity(intent);
        });

        binding.mediaList.setAdapter(adapter);
    }

    // refactor this shit
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

    // refactor this shit
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
                        Toast.makeText(this, "URL не може бути порожнім", Toast.LENGTH_SHORT).show();
                    }
                }))
                .setNegativeButton("Cancle", null)
                .show();
    }

    // refactor this shit
    private void openInternetMedia(String url) {
        String lower = url.toLowerCase();
        Intent intent;

        if (lower.endsWith(".mp4") || lower.contains("video")) {
            intent = new Intent(this, VideoActivity.class);
        } else if (lower.endsWith(".mp3") || lower.contains("audio")) {
            intent = new Intent(this, AudioActivity.class);
        } else {
            Toast.makeText(this, "Невідомий тип файлу", Toast.LENGTH_SHORT).show();
            return;
        }

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