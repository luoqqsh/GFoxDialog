package com.xing.gfox.rxHttp.task;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class TaskSimpleManager {

    private Disposable mDisposable;

    public TaskSimpleManager() {
    }

    public TaskSimpleManager start() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(String s) {
                        runOnUIThread();
                        cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
        return this;
    }

    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private Observable<String> getObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) {
                runOnBackgroundThread();
                observableEmitter.onNext("");
                observableEmitter.onComplete();
            }
        });
    }

    public abstract void runOnBackgroundThread();

    public abstract void runOnUIThread();
}