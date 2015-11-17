package com.laimiux.youtubeplayerexample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SearchActivity extends FragmentActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    if(savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.search_container, new SearchFragment())
          .commit();
    }
  }
}
