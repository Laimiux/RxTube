package com.laimiux.youtube;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by laimiux on 11/2/14.
 */
public class YoutubeListView extends ListView {
    // Needed variables to operate
    private YouTube mYouTube;
    private String mBrowserDevKey;
    private List<String> mYoutubeVideoIDs;

    // Requests
    private int mItemsLeftInObservable;
    private Observable<String> mObservableEmitsVideoIDs;

    private int mItemsPerPage = 10;
    private boolean mIsLoadingVideos;

    // Adapter
    private List<Video> mVideos;
    private BaseAdapter mAdapter;

    // State
    private int mVisibleThreshold = 5; // How many items left to scroll, when starting to download more.
    private OnScrollListener mScrollListener;


    public YoutubeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(String browserDevKey, List<String> ids, final OnListViewLoad listener) {
        if (browserDevKey == null || browserDevKey.length() == 0) {
            throw new IllegalStateException("browserDevKey cannot be null or empty");
        }

        mBrowserDevKey = browserDevKey;

        final Context context = getContext();
        if (context instanceof YouTubeProvider) {
            mYouTube = ((YouTubeProvider) context).getYouTube();
        } else if (context.getApplicationContext() instanceof YouTubeProvider) {
            mYouTube = ((YouTubeProvider) context.getApplicationContext()).getYouTube();
        } else {
            throw new IllegalStateException("Your activity or application must extend YouTubeProvider");
        }


        mYoutubeVideoIDs = ids;

        mItemsLeftInObservable = ids.size();
        mObservableEmitsVideoIDs = Observable.from(ids);


        loadPage().subscribe(new Observer<List<Video>>() {
            @Override
            public void onCompleted() {
                listener.onLoad();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onNext(List<Video> videos) {

            }
        });


        setInfiniteScroll();
    }


    private void setInfiniteScroll() {
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mScrollListener != null) {
                    mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
                    loadNextPage();
                }
            }
        });

    }

    private void loadNextPage() {
        if (!mIsLoadingVideos) {
            loadPage();
        }
    }

    private Observable<List<Video>> loadPage() {
        if (mItemsLeftInObservable == 0) {
            return Observable.empty();
        } else {
            mIsLoadingVideos = true;
            final Observable<List<Video>> listObservable = mObservableEmitsVideoIDs
                    .take(mItemsPerPage)
                    .flatMap(new Func1<String, Observable<VideoListResponse>>() {
                        @Override
                        public Observable<VideoListResponse> call(final String videoId) {
                            return getVideoListResponseObservable(videoId);
                        }
                    })
                    .map(new Func1<VideoListResponse, List<Video>>() {
                        @Override
                        public List<Video> call(VideoListResponse videoListResponse) {
                            return videoListResponse.getItems();
                        }
                    })
                    .reduce(new Func2<List<Video>, List<Video>, List<Video>>() {
                        @Override
                        public List<Video> call(List<Video> videos, List<Video> videos2) {
                            videos.addAll(videos2);
                            return videos;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache();

            listObservable
                    .subscribe(new Action1<List<Video>>() {
                        @Override
                        public void call(List<Video> videoListResponses) {

                            mItemsLeftInObservable -= mItemsPerPage;

                            if (mItemsLeftInObservable <= 0) {
                                mItemsLeftInObservable = 0;
                                mObservableEmitsVideoIDs = Observable.empty();
                            } else {
                                mObservableEmitsVideoIDs = mObservableEmitsVideoIDs.skip(mItemsPerPage);
                            }

                            if (mAdapter == null) {
                                mVideos = videoListResponses;
                                mAdapter = createAdapter(videoListResponses);
                                setAdapter(mAdapter);
                            } else {
                                mVideos.addAll(videoListResponses);
                                mAdapter.notifyDataSetChanged();
                            }

                            mIsLoadingVideos = false;
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mIsLoadingVideos = false;
                            Log.e("YoutubeListView", "Failed to load VideoListResponses", throwable);
                        }
                    });

            return listObservable;
        }
    }


    protected BaseAdapter createAdapter(List<Video> videoListResponses) {
        return new SimpleYoutubeListAdapter(getContext(), videoListResponses);
    }

    private Observable<VideoListResponse> getVideoListResponseObservable(final String videoId) {
        return Observable.create(new Observable.OnSubscribe<VideoListResponse>() {
            @Override
            public void call(Subscriber<? super VideoListResponse> subscriber) {
                try {
                    final YouTube.Videos.List contentDetails;
                    contentDetails = mYouTube.videos().list("snippet");
                    contentDetails.setKey(mBrowserDevKey);
                    contentDetails.setId(videoId);

                    final VideoListResponse videoListResponse = contentDetails.execute();

                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(videoListResponse);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }

                }

            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.d("YoutubeListView", "onAttachedToWindow()");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Log.d("YoutubeListView", "onDetachedFromWindow()");
    }


    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        // Do not call super to keep our infinite scroll listener
        mScrollListener = listener;
    }

    public static interface OnListViewLoad {
        public void onLoad();

        public void onError(Throwable error);
    }
}
