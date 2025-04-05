package com.bodkasoft.mediaplayer.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bodkasoft.mediaplayer.databinding.ItemMediaBinding;
import com.bodkasoft.mediaplayer.item.MediaItem;

import java.util.List;

public class MediaAdapter extends BaseAdapter {

    private final List<MediaItem> mediaList;
    private final OnItemClickListener listener;

    public MediaAdapter(List<MediaItem> mediaList, OnItemClickListener listener) {
        this.mediaList = mediaList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemMediaBinding binding;
        if (convertView == null) {
            binding = ItemMediaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemMediaBinding) convertView.getTag();
        }

        MediaItem item = mediaList.get(position);
        binding.mediaItemName.setText(item.getName());

        binding.openButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        return convertView;
    }

    public interface OnItemClickListener {
        void onItemClick(MediaItem item);
    }
}
