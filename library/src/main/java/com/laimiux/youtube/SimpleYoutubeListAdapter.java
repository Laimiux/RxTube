package com.laimiux.youtube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
* Created by laimiux on 11/3/14.
*/
public class SimpleYoutubeListAdapter extends BindableAdapter<Video> {
    private List<Video> mVideoListResponses;

    public SimpleYoutubeListAdapter(Context context, List<Video> videoListResponses) {
        super(context);
        mVideoListResponses = videoListResponses;
    }

    @Override
    public int getCount() {
        return mVideoListResponses.size();
    }

    @Override
    public Video getItem(int position) {
        return mVideoListResponses.get(position);
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
        ImageView imageView = (ImageView) view.findViewById(R.id.youtube_video_thumbnail);

        Picasso.with(getContext())
                .load(item.getSnippet().getThumbnails().getMedium().getUrl())
                .into(imageView);

        TextView titleTextView = (TextView) view.findViewById(R.id.youtube_video_title);
        titleTextView.setText(item.getSnippet().getTitle());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.youtube_video_description);
        descriptionTextView.setText(item.getSnippet().getDescription());

    }
}
