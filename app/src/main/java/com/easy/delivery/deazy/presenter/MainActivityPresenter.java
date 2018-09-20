package com.easy.delivery.deazy.presenter;

import android.app.Application;

import com.easy.delivery.deazy.BaseApplication;
import com.easy.delivery.deazy.model.DeliveryItem;
import com.easy.delivery.deazy.repository.remote.DeliveryService;
import com.easy.delivery.deazy.ui.MainActivityView;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivityPresenter {

    private MainActivityView view;
    @Inject
    Retrofit retrofit;


    public MainActivityPresenter(Application application, MainActivityView view) {
        ((BaseApplication) application).getNetworkComponent().inject(this);
        this.view = view;
    }


    public void loadDeliveries(int offset) {

        Call<List<DeliveryItem>> posts = retrofit.create(DeliveryService.class).getPosts(offset, 20);
        posts.enqueue(new Callback<List<DeliveryItem>>() {
            @Override
            public void onResponse(Call<List<DeliveryItem>> call, Response<List<DeliveryItem>> response) {
                if (response!=null  && response.body()!=null) {
                    view.displayDeliveries(response.body());
                }else{
                    view.displayErrors(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<DeliveryItem>> call, Throwable t) {
                //add error logs to cathartics
                view.displayErrors(t.getMessage());
            }
        });
    }
}
