package com.laimiux.youtubeplayerexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.api.services.youtube.model.Video;

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
        ids.add("EU56blypaVI");
        ids.add("i1XzCJ9yEf0");
        ids.add("QD7qIthSdkA");
        ids.add("GtKnRFNffsI");
        ids.add("IIA1XQnAv5s");
        ids.add("6vopR3ys8Kw");

        mYoutubeListView.setKey(BuildConfig.YOUTUBE_BROWSER_DEV_KEY);
        mYoutubeListView.setYoutubeVideoIDs(ids);

        mYoutubeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Video itemAtPosition = (Video) parent.getItemAtPosition(position);

                Intent intent = new Intent(ListViewActivity.this, PlayerActivity.class);
                intent.putExtra("video_id", itemAtPosition.getId());
                startActivity(intent);
            }
        });
    }
}
