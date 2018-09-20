package com.easy.delivery.deazy.ui;

import android.app.Application;

import com.easy.delivery.deazy.BaseApplication;
import com.easy.delivery.deazy.model.DeliveryItem;
import com.easy.delivery.deazy.presenter.MainActivityPresenter;
import com.easy.delivery.deazy.repository.remote.DeliveryService;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityPresenterTest {


    @Test
    public void shouldPass() {
        Assert.assertEquals(1, 1);
    }

    @Mock
    MainActivityView view;
    @Mock
    DeliveryService service;

    @Test public void shouldPassDeliveriesToView(){

        List<DeliveryItem> itemsList =  Arrays.asList(new DeliveryItem(""), new DeliveryItem(""));

        Mockito.when(service.getPostsc(0,2)).thenReturn(itemsList);

        MainActivityPresenter presenter = new MainActivityPresenter(, view);
        presenter.loadDeliveries(0);

        Mockito.verify(view).displayDeliveries(itemsList);

    }




/*
    @Test
    public void shouldPassDeliveriesToView() {
        //given
        MainActivityView view = new MockView();
        DeliveryRepository deliveryRepository = new MockDeliveryRepository(true);
        //when
        MainActivityPresenter presenter = new MainActivityPresenter(getApplication(), view, deliveryRepository);
        presenter.loadDeliveries(0);
        //then
        Assert.assertEquals(true, ((MockView) view).displayDeliveryItemsWithDeliveriesCalled);

    }

    @Test
    public void shouldHandleNoDeliveriesFound() {

        //given
        MainActivityView view = new MockView();
        DeliveryRepository deliveryRepository = new MockDeliveryRepository(false);

        //when
        MainActivityPresenter presenter = new MainActivityPresenter(getApplication(), view, deliveryRepository);
        presenter.loadDeliveries(0);

        //then
        Assert.assertEquals(true, ((MockView) view).messageShown);
    }

    private class MockView implements MainActivityView {

        boolean displayDeliveryItemsWithDeliveriesCalled;
        boolean messageShown;

        @Override
        public void displayDeliveries(List<DeliveryItem> deliveryItemsList) {
            displayDeliveryItemsWithDeliveriesCalled = true;
        }

        @Override
        public void displayErrors(String message) {
            messageShown = true;
        }
    }

    private class MockDeliveryRepository implements DeliveryRepository {

        private final boolean returnSomeItems;

        public MockDeliveryRepository(boolean returnSomeItems) {
            this.returnSomeItems = returnSomeItems;
        }

        @Override
        public List<DeliveryItem> getDeliveries() {
            if (returnSomeItems) {
                return Arrays.asList(new DeliveryItem(""), new DeliveryItem(""), new DeliveryItem(""));
            } else {
                return Collections.emptyList();
            }
        }
    }*/

}