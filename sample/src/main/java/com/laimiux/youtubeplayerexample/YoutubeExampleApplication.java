package com.laimiux.youtubeplayerexample;

import android.app.Application;
import android.content.Context;

import com.google.api.services.youtube.YouTube;
import com.laimiux.rxyoutube.RxTube;

/**
 * Created by laimiux on 11/2/14.
 */
public class YoutubeExampleApplication extends Application {
    private RxTube rxTube;

    @Override
    public void onCreate() {
        super.onCreate();

        // For more customization, look into this method and use it's code as starter.
        YouTube youtube = YouTubeUtil.createDefaultYouTube("youtube-example-application");
        rxTube = new RxTube(youtube, BuildConfig.YOUTUBE_BROWSER_DEV_KEY);
    }


    public RxTube getYouTube() {
        return rxTube;
    }

  public static YoutubeExampleApplication get(Context context) {
    return (YoutubeExampleApplication) context.getApplicationContext();
  }
}
