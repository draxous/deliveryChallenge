package com.easy.delivery.deazy.api;

import com.easy.delivery.deazy.api.model.DeliveryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {

    @GET("/deliveries")
    Call<List<DeliveryItem>> getPosts(@Query("offset") int offset, @Query("limit") int limit);

}