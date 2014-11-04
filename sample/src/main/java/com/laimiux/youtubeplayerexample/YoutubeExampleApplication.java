package com.laimiux.youtubeplayerexample;

import android.app.Application;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.laimiux.youtube.YoutubeListView;

import java.io.IOException;

/**
 * Created by laimiux on 11/2/14.
 */
public class YoutubeExampleApplication extends Application implements YoutubeListView.YouTubeProvider {
    private YouTube mYouTube;

    @Override
    public void onCreate() {
        super.onCreate();
        // This object is used to make YouTube Data API requests. The last
        // argument is required, but since we don't need anything
        // initialized when the HttpRequest is initialized, we override
        // the interface and provide a no-op function.
        mYouTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("youtube-example-application").build();
    }


    @Override
    public YouTube getYouTube() {
        return mYouTube;
    }
}
