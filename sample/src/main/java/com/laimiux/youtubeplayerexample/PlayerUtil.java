package com.laimiux.youtubeplayerexample;

import android.content.Context;

public class PlayerUtil {


  private PlayerUtil() {
  }


  public static void showPlayer(Context context, String videoId) {
    PlayerActivity.showPlayer(context, BuildConfig.YOUTUBE_DEV_KEY, videoId);
  }
}
