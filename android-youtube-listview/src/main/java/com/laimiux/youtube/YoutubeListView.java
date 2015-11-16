package com.laimiux.youtube;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.laimiux.rxyoutube.RxTube;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by laimiux on 11/2/14.
 */
public class YoutubeListView extends ListView {
  // Needed variables to operate
  private RxTube youtube;
  private List<String> videoIds;

  // Requests
  private int itemsLeftInObservable;
  private Observable<String> videoIdStream;

  private int itemsPerPage = 10;
  private boolean isLoadingVideos;

  // Adapter
  private List<Video> videos;
  private BaseAdapter adapter;

  // State
  private int visibleThreshold = 5; // How many items left to scroll, when starting to download more.
  private OnScrollListener scrollListener;


  public YoutubeListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void init(RxTube tube, List<String> ids, final OnListViewLoad listener) {
    if (tube == null) {
      throw new IllegalStateException("tube cannot be null");
    }

    this.youtube = tube;
    videoIds = ids;

    itemsLeftInObservable = ids.size();
    videoIdStream = Observable.from(ids);


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
        if (scrollListener != null) {
          scrollListener.onScrollStateChanged(view, scrollState);
        }
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollListener != null) {
          scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
          loadNextPage();
        }
      }
    });

  }

  private void loadNextPage() {
    if (!isLoadingVideos) {
      loadPage();
    }
  }

  private Observable<List<Video>> loadPage() {
    if (itemsLeftInObservable == 0) {
      return Observable.empty();
    } else {
      isLoadingVideos = true;
      final Observable<List<Video>> listObservable = videoIdStream
          .take(itemsPerPage)
          .reduce("", new Func2<String, String, String>() {
            @Override public String call(String s, String s2) {
              if (!TextUtils.isEmpty(s)) {
                return s + "," + s2;
              }

              return s2;
            }
          })
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
          }).observeOn(AndroidSchedulers.mainThread()).cache();

      listObservable.subscribe(new Action1<List<Video>>() {
        @Override
        public void call(List<Video> videoListResponses) {
          itemsLeftInObservable -= itemsPerPage;

          if (itemsLeftInObservable <= 0) {
            itemsLeftInObservable = 0;
            videoIdStream = Observable.empty();
          } else {
            videoIdStream = videoIdStream.skip(itemsPerPage);
          }

          if (adapter == null) {
            videos = videoListResponses;
            adapter = createAdapter(videoListResponses);
            setAdapter(adapter);
          } else {
            videos.addAll(videoListResponses);
            adapter.notifyDataSetChanged();
          }

          isLoadingVideos = false;
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          isLoadingVideos = false;
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
    return youtube.create(new RxTube.Query<YouTube.Videos.List>() {
      @Override public YouTube.Videos.List create(YouTube youTube) throws Exception {
        final YouTube.Videos.List query = youTube.videos().list("snippet");
        query.setId(videoId);
        return query;
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
    scrollListener = listener;
  }

  public interface OnListViewLoad {
    void onLoad();

    void onError(Throwable error);
  }
}
