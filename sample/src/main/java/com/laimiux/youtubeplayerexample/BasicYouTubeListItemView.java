package com.laimiux.youtubeplayerexample;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.api.services.youtube.model.Video;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by laimiux on 11/5/14.
 */
public class BasicYouTubeListItemView extends RelativeLayout {
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    // Request
    private RequestCreator mRequest;
    private int mWidth;
    private int mHeight;


    public BasicYouTubeListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        imageView = (ImageView) findViewById(R.id.youtube_video_thumbnail);
        titleTextView = (TextView) findViewById(R.id.youtube_video_title);
        descriptionTextView = (TextView) findViewById(R.id.youtube_video_description);

        final Resources resources = getResources();
        mWidth = resources.getDimensionPixelSize(R.dimen.youtube_image_width);
        mHeight = resources.getDimensionPixelSize(R.dimen.youtube_image_height);

    }

    public void bindView(Picasso picasso, Video video) {
        titleTextView.setText(video.getSnippet().getTitle());
        descriptionTextView.setText(video.getSnippet().getDescription());


        mRequest = picasso.load(video.getSnippet().getThumbnails().getMedium().getUrl());

        requestLayout();
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mRequest != null) {
            mRequest.resize(mWidth, mHeight).noFade().centerCrop().into(imageView);
            mRequest = null;
        }
    }
}
