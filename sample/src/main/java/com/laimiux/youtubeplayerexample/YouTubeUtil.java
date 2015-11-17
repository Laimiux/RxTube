package com.laimiux.youtubeplayerexample;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;

/**
 * Created by laimiux on 11/3/14.
 */
public class YouTubeUtil {

    public static YouTube createDefaultYouTube(String applicationName) {
        return new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(applicationName).build();
    }
}
