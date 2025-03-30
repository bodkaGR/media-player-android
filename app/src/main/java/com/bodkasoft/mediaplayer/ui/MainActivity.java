package com.bodkasoft.mediaplayer.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bodkasoft.mediaplayer.R;
import com.bodkasoft.mediaplayer.databinding.ActivityMainBinding;
import com.bodkasoft.mediaplayer.viewmodel.PlayerViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PlayerViewModel viewModel;
    private static final int IDM_OPEN = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = this.getActionBar();

        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        // Обробники подій для аудіо
        binding.playAudio.setOnClickListener(v -> viewModel.playAudio());
        binding.pauseAudio.setOnClickListener(v -> viewModel.pauseAudio());
        binding.stopAudio.setOnClickListener(v -> viewModel.stopAudio());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_OPEN, 1, "Open")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case IDM_OPEN:
                Intent intentVideo = new Intent(MainActivity.this, MediaListActivity.class);
                startActivity(intentVideo);
                break;
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}