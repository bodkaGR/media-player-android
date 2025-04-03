package com.bodkasoft.mediaplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bodkasoft.mediaplayer.databinding.ActivityMediaListBinding;
import com.bodkasoft.mediaplayer.item.MediaItem;
import com.bodkasoft.mediaplayer.reader.UrisReader;
import com.bodkasoft.mediaplayer.ui.adapter.MediaAdapter;
import com.bodkasoft.mediaplayer.utils.MediaType;

import java.util.ArrayList;
import java.util.List;

public class MediaListActivity extends AppCompatActivity {
    private ActivityMediaListBinding binding;
    private List<MediaItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMediaListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        UrisReader reader = new UrisReader(this);

        items.addAll(reader.getAllAudioItems());
        items.addAll(reader.getAllVideoItems());
        items.addAll(reader.getInternalMediaItems());

        binding.mediaList.setAdapter(new MediaAdapter(items, item -> {
            Intent intent = (item.directoryType().equals(MediaType.VIDEO))
                    ? new Intent(this, VideoActivity.class): new Intent(this, AudioActivity.class);
            intent.putExtra("MEDIA_URI", item.getUri());
            startActivity(intent);
        }));
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