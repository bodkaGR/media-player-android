package com.bodkasoft.mediaplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bodkasoft.mediaplayer.R;
import com.bodkasoft.mediaplayer.databinding.ActivityMediaListBinding;
import com.bodkasoft.mediaplayer.item.MediaItem;
import com.bodkasoft.mediaplayer.ui.adapter.MediaAdapter;

import java.util.ArrayList;
import java.util.List;

public class MediaListActivity extends AppCompatActivity {

    private ActivityMediaListBinding binding;
    private List<MediaItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMediaListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        items = getMediaItems();

        binding.mediaList.setAdapter(new MediaAdapter(items, item -> {
            if (item.isVideo()) {
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra("MEDIA_URI", item.getUri());
                intent.putExtra("IS_VIDEO", item.isVideo());
                startActivity(intent);
            }
        }));

    }

    private List<MediaItem> getMediaItems() {
        return List.of(
                new MediaItem("kachok", "android.resource://" + getPackageName() + "/" + R.raw.kachok, true),
                new MediaItem("ai_molodets", "android.resource://" + getPackageName() + "/" + R.raw.ai_molodets, false)
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}