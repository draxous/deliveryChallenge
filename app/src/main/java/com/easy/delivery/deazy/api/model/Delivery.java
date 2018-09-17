package com.easy.delivery.deazy.api.model;

import java.util.List;

public class Delivery {

    private List<DeliveryItem> data;


    public void setData(List<DeliveryItem> data){
        this.data = data;
    }

    public List<DeliveryItem> getData(){
        return data;
    }

}
