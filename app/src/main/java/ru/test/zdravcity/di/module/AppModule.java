package ru.test.zdravcity.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.test.zdravcity.App;
import ru.test.zdravcity.di.AppScope;

@Module
public class AppModule {

    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Provides
    @Singleton
    @AppScope
    Application provideApplication() {
        return app;
    }
}
