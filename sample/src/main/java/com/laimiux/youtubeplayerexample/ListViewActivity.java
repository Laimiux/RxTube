package com.laimiux.youtubeplayerexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.api.services.youtube.model.Video;
import com.laimiux.rxyoutube.RxTube;
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
    @InjectView(R.id.progress_indicator_view) ProgressBar mProgressBar;

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
        ids.add("4ZHwu0uut3k");
        ids.add("b6dD-I7kJmM");
        ids.add("NDH1bGnNMjw");
        ids.add("rnqUBmd5xRo");
        ids.add("fJ5LaPyzaj0");
        ids.add("6teOmBuMxw4");
        ids.add("RBumgq5yVrA");



        // Show loader here
        mProgressBar.setVisibility(View.VISIBLE);
        final RxTube rxTube = YoutubeExampleApplication.get(this).getYouTube();
        mYoutubeListView.init(rxTube, ids, new YoutubeListView.OnListViewLoad() {
            @Override public void onLoad() {
                // Hide loader here.
                mProgressBar.setVisibility(View.GONE);
            }

            @Override public void onError(Throwable error) {
                // Hide loader
                mProgressBar.setVisibility(View.GONE);

                Toast.makeText(
                    ListViewActivity.this,
                    "There was an error " + error, Toast.LENGTH_LONG).show();
            }
        });

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
