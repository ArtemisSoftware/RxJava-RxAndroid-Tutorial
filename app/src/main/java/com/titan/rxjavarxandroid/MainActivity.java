package com.titan.rxjavarxandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.titan.rxjavarxandroid.models.Task;
import com.titan.rxjavarxandroid.util.DataSource;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    //ui
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);

        Observable<Task> taskObservable = Observable
            .fromIterable(DataSource.createTasksList())
            .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {

                        Timber.d("test: Thread: " + Thread.currentThread().getName());

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return task.isComplete();
                    }
                })
            .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {
                Timber.d("onSubscribe: called");
            }

            @Override
            public void onNext(Task task) {
                Timber.d("onNext: Thread: " + Thread.currentThread().getName());
                Timber.d("onNext: " + task.getDescription());

            }

            @Override
            public void onError(Throwable e) {
                Timber.e("onError: " + e);
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete: called");
            }
        });
    }
}
