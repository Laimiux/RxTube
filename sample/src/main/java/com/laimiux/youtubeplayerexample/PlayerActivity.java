package com.laimiux.youtubeplayerexample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity {
  private static final int RECOVERY_DIALOG_REQUEST = 1;

  public static final String YOUTUBE_DEV_KEY_EXTRA = "youtube_dev_key";
  public static final String VIDEO_ID_EXTRA = "video_id";

  private YouTubePlayerView youtubePlayerView;

  // Video variables.
  private String youtubeDevKey;
  private String videoId;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    youtubeDevKey = getIntent().getStringExtra(YOUTUBE_DEV_KEY_EXTRA);

    if (TextUtils.isEmpty(youtubeDevKey)) {
      throw new IllegalStateException("You need to pass a valid youtube_dev_key");
    }

    videoId = getIntent().getStringExtra(VIDEO_ID_EXTRA);

    if (TextUtils.isEmpty(videoId)) {
      throw new IllegalStateException("You need to pass a valid video_id");
    }

    setContentView(R.layout.youtube_player_view_container);

    youtubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);

    initializeVideo();
  }

  private void initializeVideo() {
    youtubePlayerView.initialize(youtubeDevKey, new YouTubePlayer.OnInitializedListener() {
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
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    youtubePlayerView.onConfigurationChanged(newConfig);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
//            // Retry initialization if user performed a recovery action
      initializeVideo();
    }
  }

  public static void showPlayer(Activity activity, String youtubeDevKey, String videoId) {
    Intent intent = new Intent(activity, PlayerActivity.class);
    intent.putExtra(YOUTUBE_DEV_KEY_EXTRA, youtubeDevKey);
    intent.putExtra(VIDEO_ID_EXTRA, videoId);
    activity.startActivity(intent);
  }
}
