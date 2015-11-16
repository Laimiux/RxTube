package com.laimiux.rxyoutube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequest;

import java.io.IOException;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class RxTube {
  private final YouTube youtube;
  private final String browserKey;
  private final Scheduler backgroundScheduler;

  private final RxVideos rxVideos;


  public RxTube(YouTube youtube, String browserKey) {
    this.youtube = youtube;
    this.browserKey = browserKey;
    backgroundScheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    rxVideos = new RxVideos(youtube.videos(), backgroundScheduler);
  }


  public RxVideos videos() {
    return rxVideos;
  }

  public <T> Observable<T> execute(final YouTubeRequest<T> request) {
    // Decorate request
    request.setKey(browserKey);
    return Observable.defer(new Func0<Observable<T>>() {
      @Override public Observable<T> call() {
        try {
          final T response = request.execute();
          return Observable.just(response);
        } catch (IOException e) {
          return Observable.error(e);
        }
      }
    }).subscribeOn(backgroundScheduler).observeOn(backgroundScheduler);
  }
}
