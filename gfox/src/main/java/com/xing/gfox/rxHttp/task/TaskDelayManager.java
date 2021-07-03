package com.xing.gfox.rxHttp.task;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public abstract class TaskDelayManager {
    /**
     * 主线程使用
     */
    private Disposable mDisposable;
    private long loopMs;

    public TaskDelayManager() {
    }

    public TaskDelayManager loop(long delayMs, long loopMs) {
        doWork(getLoopObservable(delayMs, loopMs));
        return this;
    }

    public TaskDelayManager loop(long loopMs) {
        doWork(getLoopObservable(loopMs));
        return this;
    }

    public TaskDelayManager delay(long delayMs) {
        doWork(getDelayObservable(delayMs));
        return this;
    }

    public TaskDelayManager start() {
        doWork(getDelayObservable(0));
        return this;
    }

    private void doWork(Observable<Long> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(Long index) {
//                        aLong 0,1,2,3,4,5,6,7,8...
                        onListen(index);
                        if (loopMs == 0) {
                            cancel();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (throwable != null) throwable.printStackTrace();
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
    }

    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private Observable<Long> getDelayObservable(long delayMs) {
        this.loopMs = 0;
        return Observable.timer(delayMs, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> getLoopObservable(long delayMs, long loopMs) {
        this.loopMs = loopMs;
        return Observable.interval(delayMs, loopMs, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> getLoopObservable(long loopMs) {
        if (loopMs <= 0) loopMs = 1;
        this.loopMs = loopMs;
        return Observable.interval(loopMs, TimeUnit.MILLISECONDS);
    }

    public abstract void onListen(Long index);
}