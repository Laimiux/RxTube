package com.laimiux.rxyoutube;

import com.google.api.services.youtube.YouTube;

import java.io.IOException;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func0;

public class RxVideos {
  private final YouTube.Videos videos;
  private final Scheduler backgroundScheduler;

  RxVideos(YouTube.Videos videos, Scheduler backgroundScheduler) {
    this.videos = videos;
    this.backgroundScheduler = backgroundScheduler;
  }



  public Observable<YouTube.Videos.List> list(final String part) {
    return Observable.defer(new Func0<Observable<YouTube.Videos.List>>() {
      @Override public Observable<YouTube.Videos.List> call() {
        try {
          final YouTube.Videos.List list = videos.list(part);
          return Observable.just(list);
        } catch (IOException e) {
          return Observable.error(e);
        }
      }
    }).subscribeOn(backgroundScheduler).observeOn(backgroundScheduler);
  }



}
