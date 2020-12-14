package ru.test.zdravcity.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import ru.test.zdravcity.ui.MainContract;
import ru.test.zdravcity.ui.MainPresenter;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    MainContract.Presenter providePresenter() {
        return new MainPresenter();
    }
}
