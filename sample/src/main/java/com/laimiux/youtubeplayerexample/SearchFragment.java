package com.laimiux.youtubeplayerexample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.laimiux.rxyoutube.RxTube;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class SearchFragment extends Fragment {
  @InjectView(R.id.youtube_list_view) ListView listView;
  @InjectView(R.id.progress_indicator_view) ProgressBar progressBar;

  private RxTube rxTube;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.youtube_list_view, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Video video = (Video) parent.getItemAtPosition(position);
        PlayerUtil.showPlayer(getContext(), video.getId());
      }
    });
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    rxTube = YoutubeExampleApplication.get(getContext()).getYouTube();


    progressBar.setVisibility(View.VISIBLE);
    rxTube.create(new RxTube.Query<YouTube.Search.List>() {
      @Override public YouTube.Search.List create(YouTube youTube) throws Exception {
        final YouTube.Search.List request = youTube.search().list("snippet");
        request.setQ("funny cats");
        request.setType("video");
        request.setMaxResults(20L);
        return request;
      }
    }).map(new Func1<SearchListResponse, String>() {
      @Override public String call(SearchListResponse response) {
        final List<SearchResult> results = response.getItems();

        StringBuilder builder = new StringBuilder();
        for (SearchResult result : results) {
          if (builder.length() > 0) {
            builder.append(',');
          }
          builder.append(result.getId().getVideoId());
        }

        return builder.toString();
      }
    }).flatMap(new Func1<String, Observable<VideoListResponse>>() {
      @Override public Observable<VideoListResponse> call(final String ids) {
        return rxTube.create(new RxTube.Query<YouTube.Videos.List>() {
          @Override public YouTube.Videos.List create(YouTube youTube) throws Exception {
            final YouTube.Videos.List request = youTube.videos().list("snippet");
            request.setId(ids);
            return request;
          }
        });
      }
    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<VideoListResponse>() {
      @Override public void call(VideoListResponse response) {
        progressBar.setVisibility(View.GONE);
        if (getContext() != null) {
          listView.setAdapter(new SimpleYoutubeListAdapter(getContext(), response.getItems()));
        }

      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        Log.e("Search", "failure: " + throwable, throwable);
        // TO-DO handle error
      }
    });
  }
}
