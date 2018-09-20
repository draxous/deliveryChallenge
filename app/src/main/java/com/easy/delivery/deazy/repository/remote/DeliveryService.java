package com.easy.delivery.deazy.repository.remote;

import com.easy.delivery.deazy.model.DeliveryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DeliveryService {

    @GET("/deliveries")
    Call<List<DeliveryItem>> getPosts(@Query("offset") int offset, @Query("limit") int limit);

    @GET("/deliveries")
    List<DeliveryItem> getPostsc(@Query("offset") int offset, @Query("limit") int limit);

}