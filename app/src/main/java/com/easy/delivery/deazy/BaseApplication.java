package com.easy.delivery.deazy;

import android.app.Application;

import com.easy.delivery.deazy.di.component.DaggerNetworkComponent;
import com.easy.delivery.deazy.di.component.NetworkComponent;
import com.easy.delivery.deazy.di.module.ApplicationModule;
import com.easy.delivery.deazy.di.module.NetworkModule;

public class BaseApplication extends Application {

    private NetworkComponent mNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkComponent = DaggerNetworkComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(BuildConfig.BASE_URL))
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }
}

