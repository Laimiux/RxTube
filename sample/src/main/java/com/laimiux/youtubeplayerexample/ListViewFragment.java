package com.laimiux.youtubeplayerexample;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.laimiux.rxyoutube.RxTube;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListViewFragment extends Fragment {
  @InjectView(R.id.youtube_list_view) ListView listView;
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
  }
}
