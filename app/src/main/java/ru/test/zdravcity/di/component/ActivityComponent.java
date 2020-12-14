package ru.test.zdravcity.di.component;

import dagger.Component;
import ru.test.zdravcity.di.module.ActivityModule;
import ru.test.zdravcity.ui.MainActivity;

@Component(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
