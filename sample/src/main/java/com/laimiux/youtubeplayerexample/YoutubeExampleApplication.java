package com.laimiux.youtubeplayerexample;

import android.app.Application;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.laimiux.youtube.YouTubeProvider;
import com.laimiux.youtube.YouTubeUtil;

import java.io.IOException;

/**
 * Created by laimiux on 11/2/14.
 */
public class YoutubeExampleApplication extends Application implements YouTubeProvider {
    private YouTube mYouTube;

    @Override
    public void onCreate() {
        super.onCreate();

        // For more customization, look into this method and use it's code as starter.
        mYouTube = YouTubeUtil.createDefaultYouTube("youtube-example-application");
    }


    @Override
    public YouTube getYouTube() {
        return mYouTube;
    }
}
