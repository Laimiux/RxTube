package com.laimiux.rxyoutube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequest;

import java.io.IOException;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class RxTube {
  private final YouTube youtube;
  private final String browserKey;
  private final Scheduler backgroundScheduler;

  public RxTube(YouTube youtube, String browserKey) {
    this.youtube = youtube;
    this.browserKey = browserKey;
    backgroundScheduler = Schedulers.from(Executors.newSingleThreadExecutor());
  }

  public <T extends YouTubeRequest<R>, R> Observable<R> create(final Query<T> query) {
    final Observable<R> observable = Observable.create(new Observable.OnSubscribe<R>() {
      @Override public void call(Subscriber<? super R> subscriber) {
        try {
          final T request = query.create(youtube);
          if (!subscriber.isUnsubscribed()) {
            request.setKey(browserKey);
            final R result = request.execute();
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(result);
              subscriber.onCompleted();
            }
          }
        } catch (Exception e) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onError(e);
          }
        }
      }
    });

    return observable.subscribeOn(backgroundScheduler).observeOn(backgroundScheduler);
  }

  public interface Query<T extends YouTubeRequest> {
    T create(YouTube youTube) throws Exception;
  }
}
