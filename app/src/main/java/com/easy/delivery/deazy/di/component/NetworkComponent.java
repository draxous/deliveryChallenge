package com.easy.delivery.deazy.di.component;

import com.easy.delivery.deazy.di.module.ApplicationModule;
import com.easy.delivery.deazy.di.module.NetworkModule;
import com.easy.delivery.deazy.ui.MainActivity;
import com.easy.delivery.deazy.presenter.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject(MainActivity activity);
    void inject(MainActivityPresenter presenter);
}
