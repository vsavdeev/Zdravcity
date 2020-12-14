package ru.test.zdravcity;

import android.app.Application;

import ru.test.zdravcity.di.component.AppComponent;
import ru.test.zdravcity.di.component.DaggerAppComponent;
import ru.test.zdravcity.di.module.AppModule;


public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        configureDagger();
    }

    private void configureDagger(){
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
