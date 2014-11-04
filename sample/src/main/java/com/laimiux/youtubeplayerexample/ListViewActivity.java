package com.laimiux.youtubeplayerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.api.services.youtube.model.Video;
import com.laimiux.youtube.PlayerActivity;
import com.laimiux.youtube.YoutubeListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by laimiux on 11/2/14.
 */
public class ListViewActivity extends Activity {

    @InjectView(R.id.youtube_list_view) YoutubeListView mYoutubeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_list_view);

        ButterKnife.inject(this);

        List<String> ids = new ArrayList<String>();
        ids.add("iX-QaNzd-0Y");
        ids.add("nCkpzqqog4k");
        ids.add("pB-5XG-DbAA");
        ids.add("QD7qIthSdkA");
        ids.add("GtKnRFNffsI");
        ids.add("IIA1XQnAv5s");
        ids.add("6vopR3ys8Kw");
        ids.add("uJ_1HMAGb4k");
        ids.add("MYSVMgRr6pw");
        ids.add("oWYp1xRPH5g");
        ids.add("qlGQoxzdwP4");

        mYoutubeListView.setKey(BuildConfig.YOUTUBE_BROWSER_DEV_KEY);
        mYoutubeListView.setYoutubeVideoIDs(ids);

        mYoutubeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Video video = (Video) parent.getItemAtPosition(position);
                showVideo(video);
            }
        });
    }

    private void showVideo(Video video) {
        PlayerActivity.showPlayer(this, BuildConfig.YOUTUBE_DEV_KEY, video.getId());
    }
}
