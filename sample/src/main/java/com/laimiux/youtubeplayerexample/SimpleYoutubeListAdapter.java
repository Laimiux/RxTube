package com.laimiux.youtubeplayerexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
* Created by laimiux on 11/3/14.
*/
public class SimpleYoutubeListAdapter extends BindableAdapter<Video> {
    private List<Video> videos;
    private Picasso picasso;

    public SimpleYoutubeListAdapter(Context context, List<Video> videoListResponses) {
        super(context);

        videos = videoListResponses;
        picasso = Picasso.with(context);
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Video getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(R.layout.youtube_list_item, container, false);
    }

    @Override
    public void bindView(Video item, int position, View view) {
        ((BasicYouTubeListItemView) view).bindView(picasso, item);
    }
}
