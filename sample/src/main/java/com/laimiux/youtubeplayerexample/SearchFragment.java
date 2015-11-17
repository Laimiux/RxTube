package com.laimiux.youtubeplayerexample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.laimiux.rxyoutube.RxTube;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;

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
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    rxTube = YoutubeExampleApplication.get(getContext()).getYouTube();


    progressBar.setVisibility(View.VISIBLE);
    rxTube.create(new RxTube.Query<YouTube.Search.List>() {
      @Override public YouTube.Search.List create(YouTube youTube) throws Exception {
        final YouTube.Search.List request = youTube.search().list("snippet");
        request.setQ("funny cats");
        return request;
      }
    }).subscribe(new Action1<SearchListResponse>() {
      @Override public void call(SearchListResponse response) {
        progressBar.setVisibility(View.GONE);
        final List<SearchResult> results = response.getItems();
        Log.d("Search", "Found " + results.size() + " results");
        for (SearchResult result : results) {
          Log.d("Search", "Result: " + result);
        }
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        // TO-DO handle error
      }
    });
  }
}
