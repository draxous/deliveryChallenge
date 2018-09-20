package com.easy.delivery.deazy.ui;

import com.easy.delivery.deazy.model.DeliveryItem;

import java.util.List;

public interface MainActivityView {

    void displayDeliveries(List<DeliveryItem> deliveryItemsList);

    void displayErrors(String message);

}
