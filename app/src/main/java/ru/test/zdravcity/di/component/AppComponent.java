package ru.test.zdravcity.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.test.zdravcity.App;
import ru.test.zdravcity.di.module.AppModule;

@Component( modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);
}
