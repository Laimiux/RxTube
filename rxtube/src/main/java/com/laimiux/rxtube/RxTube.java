package com.laimiux.rxtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequest;

import java.io.IOException;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * A class responsible for creating {@linkplain Observable observables} for {@link YouTube}
 */
public final class RxTube {
  private final YouTube youtube;
  private final String browserKey;
  private final Scheduler backgroundScheduler;

  public RxTube(YouTube youtube, String browserKey) {
    this(youtube, browserKey, Schedulers.from(Executors.newSingleThreadExecutor()));
  }

  public RxTube(YouTube youtube, String browserKey, Scheduler scheduler) {
    this.youtube = youtube;
    this.browserKey = browserKey;
    this.backgroundScheduler = scheduler;
  }

  /**
   * Create an observable that performs a query and returns a response
   */
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

  /**
   *  Represents a factory that creates a YouTube request
   */
  public interface Query<T extends YouTubeRequest> {
    T create(YouTube youTube) throws Exception;
  }
}
