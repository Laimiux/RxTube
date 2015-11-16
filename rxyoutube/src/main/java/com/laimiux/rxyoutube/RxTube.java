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
  private final Scheduler backgroundScheduler;

  public RxTube(YouTube youtube) {
    this.youtube = youtube;
    backgroundScheduler = Schedulers.from(Executors.newSingleThreadExecutor());
  }


  public <T> Observable<T> execute(final YouTubeRequest<T> request) {
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
