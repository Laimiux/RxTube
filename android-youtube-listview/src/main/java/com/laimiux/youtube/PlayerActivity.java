package com.laimiux.youtube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity {
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    public static final String YOUTUBE_DEV_KEY_EXTRA = "youtube_dev_key";
    public static final String VIDEO_ID_EXTRA = "video_id";

    private YouTubePlayerView mYouTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String youtubeDevKey = getIntent().getStringExtra(YOUTUBE_DEV_KEY_EXTRA);

        if (youtubeDevKey == null || youtubeDevKey.length() == 0) {
            throw new IllegalStateException("You need to pass a valid youtube_dev_key");
        }

        final String videoId = getIntent().getStringExtra(VIDEO_ID_EXTRA);

        if (videoId == null || videoId.length() == 0) {
            throw new IllegalStateException("You need to pass a valid video_id");
        }

        setContentView(R.layout.youtube_player_view_container);

        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
        mYouTubePlayerView.initialize(youtubeDevKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {

                if (errorReason.isUserRecoverableError()) {
                    errorReason.getErrorDialog(PlayerActivity.this, RECOVERY_DIALOG_REQUEST).show();
                } else {
                    String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
                    Toast.makeText(PlayerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RECOVERY_DIALOG_REQUEST) {
//            // Retry initialization if user performed a recovery action
//            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this);
//        }
    }

    public static void showPlayer(Activity activity, String youtubeDevKey, String videoId) {
        Intent intent = new Intent(activity, PlayerActivity.class);
        intent.putExtra(YOUTUBE_DEV_KEY_EXTRA, youtubeDevKey);
        intent.putExtra(VIDEO_ID_EXTRA, videoId);
        activity.startActivity(intent);
    }
}
