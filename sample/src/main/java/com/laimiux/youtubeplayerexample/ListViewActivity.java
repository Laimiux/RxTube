package com.laimiux.youtubeplayerexample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.laimiux.rxyoutube.RxTube;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by laimiux on 11/2/14.
 */
public class ListViewActivity extends Activity {

  @InjectView(R.id.youtube_list_view) ListView listView;
  @InjectView(R.id.progress_indicator_view) ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.youtube_list_view);

    ButterKnife.inject(this);

    final List<String> ids = new ArrayList<>();
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
    progressBar.setVisibility(View.VISIBLE);
    final RxTube rxTube = YoutubeExampleApplication.get(this).getYouTube();
    rxTube.create(new RxTube.Query<YouTube.Videos.List>() {
      @Override public YouTube.Videos.List create(YouTube youTube) throws Exception {
        final YouTube.Videos.List request = youTube.videos().list("snippet");
        request.setId(TextUtils.join(",", ids));
        return request;
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<VideoListResponse>() {
      @Override public void call(VideoListResponse response) {
        // Hide loader here.
        progressBar.setVisibility(View.GONE);

        listView.setAdapter(new SimpleYoutubeListAdapter(
            ListViewActivity.this, response.getItems()));
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        // Hide loader here.
        progressBar.setVisibility(View.GONE);
        Toast.makeText(
            ListViewActivity.this,
            "There was an error " + throwable, Toast.LENGTH_LONG).show();
      }
    });

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
